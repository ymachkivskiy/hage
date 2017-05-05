package org.hage.platform.node.lifecycle.construct;


import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.node.lifecycle.LifecycleAction;
import org.hage.platform.node.lifecycle.LifecycleEvent;
import org.hage.platform.node.lifecycle.LifecycleState;
import org.hage.platform.node.lifecycle.LifecycleStateMachine;
import org.hage.platform.node.bus.EventBus;
import org.hage.platform.node.executors.schedule.ContinuousSerialScheduler;
import org.hage.platform.node.executors.schedule.ScheduleTask;
import org.hage.platform.node.executors.schedule.ScheduledTaskHandle;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Queues.newPriorityBlockingQueue;
import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static org.hage.util.concurrency.Locks.withReadLock;
import static org.hage.util.concurrency.Locks.withWriteLock;

@Slf4j
class LifecycleStateMachineService implements LifecycleStateMachine {

    private final PriorityBlockingQueue<EventHolder> eventQueue = newPriorityBlockingQueue();
    private final Table<LifecycleState, LifecycleEvent, TransitionDescriptor> transitionsTable;
    private final Set<LifecycleState> terminalStates;
    private final LifecycleEvent failureEvent;

    private final boolean shutdownAfterTerminalState;

    private final EventBus eventBus;

    private final ReadWriteLock stateLock = new ReentrantReadWriteLock();

    private final ContinuousSerialScheduler schedulerService;
    private final ScheduledTaskHandle dispatchTaskHandle;

    @GuardedBy("stateLock")
    private LifecycleState currentState;

    @GuardedBy("stateLock")
    private LifecycleEvent currentEvent;

    LifecycleStateMachineService(LifecycleStateMachineBuilder builder, ContinuousSerialScheduler continuousSerialScheduler) {
        currentState = builder.getInitialState();
        eventBus = builder.getEventBus();
        terminalStates = builder.getTerminalStates();
        shutdownAfterTerminalState = builder.isShutdownWhenTerminated();
        failureEvent = builder.getFailureBehaviorBuilder().getEvent();
        transitionsTable = buildTransitionsTable(builder);
        this.schedulerService = continuousSerialScheduler;
        this.dispatchTaskHandle = schedulerService.registerTask(new Dispatcher());
    }

    public void fire(LifecycleEvent event) {
        log.debug("{} fired.", event);
        eventQueue.add(new EventHolder(event));
        dispatchTaskHandle.resumeIfNotRunning();
    }

    public void fire(final LifecycleEvent event, final Object parameter) {
        log.debug("{} fired with parameters: {}.", event, parameter);
        eventQueue.add(new EventHolder(event, parameter));
        dispatchTaskHandle.resumeIfNotRunning();
    }

    public void fireAndWaitForTransitionToComplete(final LifecycleEvent event) {
        log.debug("{} fired. I will be waiting for the transition to complete.", event);
        EventHolder holder = new EventHolder(event);
        eventQueue.add(holder);
        dispatchTaskHandle.resumeIfNotRunning();
        holder.getSemaphore().acquireUninterruptibly();
        log.debug("{} transition completed.", event);
    }

    public boolean terminated() {
        return readLock(() -> terminalStates.contains(currentState));
    }

    public boolean isTerminating() {
        return readLock(() -> {
            if (currentEvent == null) {
                return false;
            }
            final TransitionDescriptor descriptor = transitionsTable.get(currentState, currentEvent);
            return terminalStates.contains(descriptor.getTarget());
        });
    }

    @Override
    public String toString() {
        return readLock(() -> toStringHelper(this).addValue(currentState).addValue(currentEvent).toString());
    }

    private void drainEvents() {
        for (EventHolder holder : consumingIterable(eventQueue)) {
            log.warn("Unprocessed event {}.", holder.getEvent());
            holder.getSemaphore().release();
        }
    }

    private void setCurrentState(LifecycleState state) {
        writeLock(() -> currentState = state);
    }

    private void setCurrentEvent(LifecycleEvent event) {
        writeLock(() -> currentEvent = event);
    }

    private void internalShutdown() {
        log.debug("Service is shutting down.");
        schedulerService.shutdown();
    }

    private class Dispatcher implements ScheduleTask {

        private ListeningExecutorService localExecutor = newDirectExecutorService();

        @Override
        public boolean perform() {
            if (terminated()) {
                if (!eventQueue.isEmpty()) {
                    log.warn("Service already terminated ({}).", currentState);
                    drainEvents();
                }

                if (shutdownAfterTerminalState) {
                    internalShutdown();
                }
                return false;
            }

            assert currentEvent == null : "There is an event still running.";

            final EventHolder holder;
            final LifecycleEvent event;

            try {
                stateLock.writeLock().lock();

                if (eventQueue.isEmpty()) {
                    return false;
                }

                try {
                    holder = eventQueue.take();
                } catch (final InterruptedException e) {
                    log.debug("Interrupted when waiting for event. Returning.", e);
                    return false;
                }
                event = holder.getEvent();
                setCurrentEvent(event);

            } finally {
                stateLock.writeLock().unlock();
            }

            log.debug("In {} and firing {}.", currentState, event);
            final TransitionDescriptor transitionDescriptor = transitionsTable.get(currentState, event);
            if (transitionDescriptor.isNull()) {
                log.debug("Null transition.");
                fire(failureEvent);
                setCurrentEvent(null);
                holder.getSemaphore().release();
                return false;
            }
            log.debug("Planned transition: {}.", transitionDescriptor);

            LifecycleAction lifecycleAction = transitionDescriptor.getLifecycleAction();
            ListenableFuture<?> future = localExecutor.submit(lifecycleAction::execute);
            addCallback(future, new TransitionFinalizer(transitionDescriptor, holder.getSemaphore()));

            return true;
        }

    }

    @RequiredArgsConstructor
    private class TransitionFinalizer implements FutureCallback<Object> {

        private final TransitionDescriptor descriptor;

        private final Semaphore completionSemaphore;

        @Override
        public void onSuccess(final Object result) {
            log.debug("Transition from {} on {} to {} was successful.", descriptor.getInitial(), descriptor.getEvent(), descriptor.getTarget());
            writeLock(() -> {
                setCurrentState(descriptor.getTarget());
                setCurrentEvent(null);
            });

            eventBus.post(descriptor.createEvent());

            completionSemaphore.release();
        }

        @Override
        public void onFailure(final Throwable t) {
            log.error("Transition from {} on {} to {} failed with exception {}.", descriptor.getInitial(), descriptor.getEvent(), descriptor.getTarget(), t);
            setCurrentEvent(null);
            fire(failureEvent, t);
            completionSemaphore.release();
        }

    }

    @RequiredArgsConstructor
    private class EventHolder implements Comparable<EventHolder> {

        @Getter
        private final Semaphore semaphore = new Semaphore(0);

        @Getter
        private final LifecycleEvent event;
        @Getter
        @Nullable
        private final Object parameters;

        public EventHolder(final LifecycleEvent event) {
            this(event, null);
        }

        @Override
        public int compareTo(final EventHolder o) {
            if (failureEvent.equals(this.event) && failureEvent.equals(o.event)) {
                return 0;
            } else if (failureEvent.equals(this.event) && !failureEvent.equals(o.event)) {
                return 1;
            } else if (!failureEvent.equals(this.event) && !failureEvent.equals(o.event)) {
                return -1;
            }
            return 0;
        }

    }

    private void writeLock(final Runnable runnable) {
        withWriteLock(stateLock, runnable);
    }

    private void readLock(final Runnable runnable) {
        withReadLock(stateLock, runnable);
    }

    private <T> T readLock(final Callable<T> callable) {
        try {
            return withReadLock(stateLock, callable);
        } catch (final Exception e) {
            fire(failureEvent, e);
        }
        return null;
    }

    private static Table<LifecycleState, LifecycleEvent, TransitionDescriptor> buildTransitionsTable(LifecycleStateMachineBuilder builder) {

        final ImmutableTable.Builder<LifecycleState, LifecycleEvent, TransitionDescriptor> tableBuilder = ImmutableTable.builder();

        for (final LifecycleState state : LifecycleState.values()) {
            for (final LifecycleEvent event : LifecycleEvent.values()) {
                tableBuilder.put(state, event, builder.transitionFor(state, event));
            }
        }

        return tableBuilder.build();
    }

}
