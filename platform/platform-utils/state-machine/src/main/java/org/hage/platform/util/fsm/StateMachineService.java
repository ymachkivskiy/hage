package org.hage.platform.util.fsm;


import com.google.common.collect.Table;
import com.google.common.util.concurrent.*;
import org.hage.platform.annotation.FieldsAreNonnullByDefault;
import org.hage.platform.util.bus.EventBus;
import org.hage.util.Locks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


@FieldsAreNonnullByDefault
@ThreadSafe
public class StateMachineService<S extends Enum<S>, E extends Enum<E>> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineService.class);

    private final EnumSet<S> allStates;

    private final EnumSet<E> allEvents;

    private final Table<S, E, TransitionDescriptor<S, E>> transitionsTable;

    private final E failureEvent;

    private final ListeningScheduledExecutorService service = listeningDecorator(
            newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("fsm-srv-%d").build()));

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

    StateMachineService(final StateMachineServiceBuilder<S, E> builder) throws NoSuchMethodException {
        allStates = EnumSet.allOf(builder.getStateClass());
        allEvents = EnumSet.allOf(builder.getEventClass());
        currentState = builder.getInitialState();
        this.eventBus = builder.getEventBus();
        this.notificationCreator = builder.getNotificationCreator();
        terminalStates = builder.getTerminalStates();
        shutdownAfterTerminalState = builder.getShutdownWhenTerminated();
        failureEvent = builder.getFailureBehaviorBuilder().getEvent();
        transitionsTable = builder.buildTransitionsTable();
        dispatcherFuture = service.scheduleAtFixedRate(new Dispatcher(), 0, 1, TimeUnit.MILLISECONDS);
    }

    public void fire(final E event) {
        log.debug("{} fired.", event);
        eventQueue.add(new EventHolder(event));
    }

    public void fire(final E event, final Object... parameters) {
        log.debug("{} fired with parameters: {}.", event, parameters);
        eventQueue.add(new EventHolder(event, parameters));
    }

    public final S getCurrentState() {
        return withReadLock(() -> currentState);
    }

    private void setCurrentState(final S state) {
        withWriteLock(() -> StateMachineService.this.currentState = state);
    }

    private void withWriteLock(final Runnable runnable) {
        try {
            Locks.withWriteLock(stateLock, runnable);
        } catch (final Exception e) {
            fire(failureEvent, e);
        }
    }

    public void fire(final E event, final Object parameter) {
        log.debug("{} fired with parameters: {}.", event, parameter);
        eventQueue.add(new EventHolder(event, parameter));
    }

    private void setCurrentEvent(@Nullable final E event) {
        withWriteLock(() -> StateMachineService.this.currentEvent = event);
    }

    public final boolean inState(final S state) {
        return withReadLock(() -> currentState == state);
    }

    @Nullable
    private <T> T withReadLock(final Callable<T> callable) {
        try {
            return Locks.withReadLock(stateLock, callable);
        } catch (final Exception e) {
            fire(failureEvent, e);
        }
        return null;
    }

    public boolean terminated() {
        return withReadLock(() -> terminalStates.contains(currentState));
    }

    public boolean isTerminating() {
        return withReadLock(() -> {
            if (currentEvent == null) {
                return false;
            }
            final TransitionDescriptor<S, E> descriptor = transitionsTable.get(currentState, currentEvent);
            return terminalStates.contains(descriptor.getTarget());
        });
    }

    public void shutdown() {
        withReadLock(() -> checkState(terminated(), "Service has not terminated yet. Current state: %s.", getCurrentState()));
        internalShutdown();
        try {
            service.awaitTermination(2, TimeUnit.SECONDS);
            log.info("Service has been shut down properly.");
        } catch (final InterruptedException e) {
            log.warn("Interrupted during shutdown.");
            Thread.currentThread().interrupt();
        }
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
            stable = withReadLock(() -> eventQueue.isEmpty() && currentEvent == null);
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

    private void withReadLock(final Runnable runnable) {
        Locks.withReadLock(stateLock, runnable);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public String toString() {
        return withReadLock(new Callable<String>() {

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


    private class TransitionFinalizer implements FutureCallback<Object> {

        private final TransitionDescriptor<S, E> descriptor;

        private final Semaphore completionSemaphore;

        public TransitionFinalizer(final TransitionDescriptor<S, E> descriptor, final Semaphore completionSemaphore) {
            this.descriptor = descriptor;
            this.completionSemaphore = completionSemaphore;
        }

        @Override
        public void onSuccess(final Object result) {
            log.debug("Transition from {} on {} to {} was successful.", logData());
            withWriteLock(() -> {
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
                ((CallableAdapter) runnable).setParameters(holder.getParameters());
            }
            final ListenableFuture<?> future = localExecutor.submit(runnable);
            Futures.addCallback(future, new TransitionFinalizer(transitionDescriptor, holder.getSemaphore()));

        }
    }


    private class EventHolder implements Comparable<EventHolder> {

        private final E event;

        private final Semaphore semaphore = new Semaphore(0);

        @Nullable
        private final Object parameters;

        public EventHolder(final E event) {
            this(event, null);
        }

        public EventHolder(final E event, @Nullable final Object parameters) {
            this.event = event;
            this.parameters = parameters;
        }

        final E getEvent() {
            return event;
        }

        final Semaphore getSemaphore() {
            return semaphore;
        }

        final Object getParameters() {
            return parameters;
        }

        final
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

}
