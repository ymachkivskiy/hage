package org.hage.platform.util.fsm;


import com.google.common.collect.Table;
import com.google.common.util.concurrent.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.FieldsAreNonnullByDefault;
import org.hage.platform.util.bus.EventBus;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.EnumSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Queues.newPriorityBlockingQueue;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.hage.util.Locks.withReadLock;
import static org.hage.util.Locks.withWriteLock;

// TODO: 2/1/2016 refactor this mess
@FieldsAreNonnullByDefault
@ThreadSafe
@Slf4j
public class StateMachineService<S extends Enum<S>, E extends Enum<E>> {

    private final ListeningScheduledExecutorService service = listeningDecorator(
        newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("fsm-srv-%d").build()));

    private final Table<S, E, TransitionDescriptor<S, E>> transitionsTable;
    private final E failureEvent;
    private final PriorityBlockingQueue<EventHolder> eventQueue = newPriorityBlockingQueue();
    private final EnumSet<S> terminalStates;
    private final ReadWriteLock stateLock = new ReentrantReadWriteLock();
    private final ScheduledFuture<?> dispatcherFuture;
    private final boolean shutdownAfterTerminalState;
    private final EventBus eventBus;
    private final NotificationEventCreator<S, E, ? extends StateChangedEvent<S, E>> notificationCreator;

    @GuardedBy("stateLock")
    private S currentState;

    @Nullable
    @GuardedBy("stateLock")
    private E currentEvent;

    StateMachineService(final StateMachineServiceBuilder<S, E> builder) {
        currentState = builder.getInitialState();
        eventBus = builder.getEventBus();
        notificationCreator = builder.getNotificationCreator();
        terminalStates = builder.getTerminalStates();
        shutdownAfterTerminalState = builder.getShutdownWhenTerminated();
        failureEvent = builder.getFailureBehaviorBuilder().getEvent();
        transitionsTable = builder.buildTransitionsTable();
        dispatcherFuture = service.scheduleAtFixedRate(new Dispatcher(), 0, 1, TimeUnit.MILLISECONDS);
    }

    public final S getCurrentState() {
        return readLock(() -> currentState);
    }

    private void setCurrentState(final S state) {
        writeLock(() -> this.currentState = state);
    }

    private void setCurrentEvent(@Nullable final E event) {
        writeLock(() -> StateMachineService.this.currentEvent = event);
    }

    public final boolean inState(final S state) {
        return readLock(() -> currentState == state);
    }

    public boolean terminated() {
        return readLock(() -> terminalStates.contains(currentState));
    }

    public boolean isTerminating() {
        return readLock(() -> {
            if (currentEvent == null) {
                return false;
            }
            final TransitionDescriptor<S, E> descriptor = transitionsTable.get(currentState, currentEvent);
            return terminalStates.contains(descriptor.getTarget());
        });
    }

    public void shutdown() {
        readLock(() -> checkState(terminated(), "Service has not terminated yet. Current state: %s.", getCurrentState()));
        internalShutdown();
        try {
            service.awaitTermination(2, TimeUnit.SECONDS);
            log.info("Service has been shut down properly.");
        } catch (final InterruptedException e) {
            log.warn("Interrupted during shutdown.");
            Thread.currentThread().interrupt();
        }
    }

    public void fire(final E event) {
        log.debug("{} fired.", event);
        eventQueue.add(new EventHolder(event));
    }

    public void fire(final E event, final Object parameter) {
        log.debug("{} fired with parameters: {}.", event, parameter);
        eventQueue.add(new EventHolder(event, parameter));
    }

    void fireAndWaitForTransitionToComplete(final E event) {
        log.debug("{} fired. I will be waiting for the transition to complete.", event);
        final EventHolder holder = new EventHolder(event);
        eventQueue.add(holder);
        holder.getSemaphore().acquireUninterruptibly();
        log.debug("{} transition completed.", event);
    }

    void fireAndWaitForStableState(final E event) {
        log.debug("{} fired. I will be waiting for the stable state.", event);
        final EventHolder holder = new EventHolder(event);
        eventQueue.add(holder);
        holder.getSemaphore().acquireUninterruptibly();

        boolean stable = false;
        while (!stable) {
            stable = readLock(() -> eventQueue.isEmpty() && currentEvent == null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.debug("Interrupted when waiting.", e);
            }
        }
        log.debug("{} transition completed.", event);
    }

    private void internalShutdown() {
        log.debug("Service is shutting down.");
        dispatcherFuture.cancel(false);
        service.shutdown();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public String toString() {
        return readLock(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return toStringHelper(this).addValue(currentState).addValue(currentEvent).toString();
            }
        });
    }

    public void drainEvents() {
        for (EventHolder holder : consumingIterable(eventQueue)) {
            log.warn("Unprocessed event {}.", holder.getEvent());
            holder.getSemaphore().release();
        }
    }

    @RequiredArgsConstructor
    private class TransitionFinalizer implements FutureCallback<Object> {

        private final TransitionDescriptor<S, E> descriptor;

        private final Semaphore completionSemaphore;
        @Override
        public void onSuccess(final Object result) {
            log.debug("Transition from {} on {} to {} was successful.", logData());
            writeLock(() -> {
                setCurrentState(descriptor.getTarget());
                setCurrentEvent(null);
            });

            eventBus.post(notificationCreator.create(descriptor.getInitial(), descriptor.getEvent(), descriptor.getTarget()));

            completionSemaphore.release();
        }

        private Object[] logData() {
            return new Object[]{descriptor.getInitial(), descriptor.getEvent(), descriptor.getTarget()};
        }

        @Override
        public void onFailure(final Throwable t) {
            log.error("Transition from {} on {} to {} failed with exception.", logData(t));
            setCurrentEvent(null);
            fire(failureEvent, t);
            completionSemaphore.release();
        }

        private Object[] logData(final Throwable t) {
            return new Object[]{descriptor.getInitial(), descriptor.getEvent(), descriptor.getTarget(), t};
        }

    }

    private class Dispatcher implements Runnable {

        private ListeningExecutorService localExecutor = sameThreadExecutor();

        @Override
        public void run() {
            if (terminated()) {
                if (!eventQueue.isEmpty()) {
                    log.warn("Service already terminated ({}).", currentState);
                    drainEvents();
                }

                if (!service.isShutdown() && shutdownAfterTerminalState) {
                    internalShutdown();
                }
                return;
            }

            assert currentEvent == null : "There is an event still running.";

            try {
                while (eventQueue.isEmpty()) {
                    Thread.sleep(10);
                }
            } catch (final InterruptedException e) {
                log.debug("Interrupted when waiting for event. Returning.", e);
                return;
            }

            final EventHolder holder;
            final E event;
            try {
                stateLock.writeLock().lock();
                try {
                    holder = eventQueue.take();
                } catch (final InterruptedException e) {
                    log.debug("Interrupted when waiting for event. Returning.", e);
                    return;
                }
                event = holder.getEvent();
                setCurrentEvent(event);

            } finally {
                stateLock.writeLock().unlock();
            }

            log.debug("In {} and firing {}.", currentState, event);
            final TransitionDescriptor<S, E> transitionDescriptor = transitionsTable.get(currentState, event);
            if (transitionDescriptor.isNull()) {
                log.debug("Null transition.");
                fire(failureEvent);
                setCurrentEvent(null);
                holder.getSemaphore().release();
                return;
            }
            log.debug("Planned transition: {}.", transitionDescriptor);

            final Runnable runnable = transitionDescriptor.getAction();
            if (runnable instanceof CallableAdapter) {
                ((CallableAdapter) runnable).setParameter(holder.getParameters());
            }
            final ListenableFuture<?> future = localExecutor.submit(runnable);
            Futures.addCallback(future, new TransitionFinalizer(transitionDescriptor, holder.getSemaphore()));

        }

    }

    @RequiredArgsConstructor
    private class EventHolder implements Comparable<EventHolder> {

        @Getter
        private final Semaphore semaphore = new Semaphore(0);
        @Getter
        private final E event;
        @Getter
        @Nullable
        private final Object parameters;

        public EventHolder(final E event) {
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

}
