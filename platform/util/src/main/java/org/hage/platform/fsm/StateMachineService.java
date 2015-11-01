/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-08-21
 * $Id$
 */

package org.hage.platform.fsm;


import com.google.common.collect.Table;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.hage.annotation.FieldsAreNonnullByDefault;
import org.hage.bus.EventBus;
import org.hage.util.Locks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Queues.newPriorityBlockingQueue;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;


/**
 * A FSM-based service implementation.
 * <p>
 * These services should be built with {@link StateMachineServiceBuilder}.
 *
 * @param <S> the states enumeration.
 * @param <E> the events enumeration.
 * @author AGH AgE Team
 * @see StateMachineServiceBuilder
 */
@FieldsAreNonnullByDefault
@ThreadSafe
public abstract class StateMachineService<S extends Enum<S>, E extends Enum<E>> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineService.class);

    private final EnumSet<S> allStates;

    private final EnumSet<E> allEvents;

    private final Table<S, E, TransitionDescriptor<S, E>> transitionsTable;

    private final E failureEvent;

    private final ListeningScheduledExecutorService service = listeningDecorator(
            newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("fsm-srv-%d").build()));

    private final PriorityBlockingQueue<EventHolder> eventQueue = newPriorityBlockingQueue();

    private final EnumSet<S> terminalStates;

    private final Method eventCreate;

    private final ReadWriteLock stateLock = new ReentrantReadWriteLock();

    private final ScheduledFuture<?> dispatcherFuture;

    private final boolean shutdownAfterTerminalState;

    private EventBus eventBus;

    @GuardedBy("stateLock")
    private S currentState;

    @Nullable
    @GuardedBy("stateLock")
    private E currentEvent;

    /**
     * Package-protected constructor.
     * <p>
     * <p>
     * The proper way to build the service is to use the builder.
     *
     * @param builder a builder containing the state machine definition.
     */
    StateMachineService(final StateMachineServiceBuilder<S, E> builder) throws NoSuchMethodException {
        allStates = EnumSet.allOf(builder.getStateClass());
        allEvents = EnumSet.allOf(builder.getEventClass());
        currentState = builder.getInitialState();
        eventBus = builder.getEventBus();
        final Class<? extends StateChangedEvent> stateChangedEventClass = builder.getStateChangedEventClass();
        Method tmpMethod;
        try {
            tmpMethod = stateChangedEventClass.getMethod("create", builder.getStateClass(), builder.getEventClass(),
                                                         builder.getStateClass());
        } catch(final NoSuchMethodException e) {
            tmpMethod = stateChangedEventClass.getMethod("create", Enum.class, Enum.class, Enum.class);
        }
        eventCreate = tmpMethod;
        terminalStates = builder.getTerminalStates();
        shutdownAfterTerminalState = builder.getShutdownWhenTerminated();
        failureEvent = builder.getFailureBehaviorBuilder().getEvent();
        transitionsTable = builder.buildTransitionsTable();
        dispatcherFuture = service.scheduleAtFixedRate(new Dispatcher(), 0, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * Fires a specific event.
     *
     * @param event an event to proceed with.
     */
    public void fire(final E event) {
        log.debug("{} fired.", event);
        eventQueue.add(new EventHolder(event));
    }

    /**
     * Fires a specific event.
     *
     * @param event      an event to proceed with.
     * @param parameters parameters to pass to the action.
     */
    public void fire(final E event, final Object... parameters) {
        log.debug("{} fired with parameters: {}.", event, parameters);
        eventQueue.add(new EventHolder(event, parameters));
    }

    /**
     * Gets the state the service is currently in.
     *
     * @return the current state of the service.
     */
    public final S getCurrentState() {
        return withReadLock(new Callable<S>() {

            @Override
            public S call() {
                return currentState;
            }
        });
    }

    private void setCurrentState(final S state) {
        withWriteLock(new Runnable() {

            @Override
            public void run() {
                StateMachineService.this.currentState = state;
            }
        });
    }

    private void withWriteLock(final Runnable runnable) {
        try {
            Locks.withWriteLock(stateLock, runnable);
        } catch(final Exception e) {
            fire(failureEvent, e);
        }
    }

    /**
     * Fires a specific event.
     *
     * @param event     an event to proceed with.
     * @param parameter a parameter to pass to the action.
     */
    public void fire(final E event, final Object parameter) {
        log.debug("{} fired with parameters: {}.", event, parameter);
        eventQueue.add(new EventHolder(event, parameter));
    }

    private void setCurrentEvent(@Nullable final E event) {
        withWriteLock(new Runnable() {

            @Override
            public void run() {
                StateMachineService.this.currentEvent = event;
            }
        });
    }

    /**
     * Checks atomically whether the service is in the given state.
     *
     * @param state a state to check.
     * @return true if the service is in the given state, false otherwise.
     */
    public final boolean inState(final S state) {
        return withReadLock(new Callable<Boolean>() {

            @Override
            public Boolean call() {
                return currentState == state;
            }
        });
    }

    @Nullable
    private <T> T withReadLock(final Callable<T> callable) {
        try {
            return Locks.withReadLock(stateLock, callable);
        } catch(final Exception e) {
            fire(failureEvent, e);
        }
        return null;
    }

    /**
     * Registers a listener to the service events.
     *
     * @param listener a listener.
     * @see EventBus#register(Object)
     */
    public void register(final Object listener) {
        eventBus.register(listener);
    }

    /**
     * Unregisters a listener from the service events.
     *
     * @param listener a listener.
     * @see EventBus#unregister(Object)
     */
    public void unregister(final Object listener) {
        eventBus.unregister(listener);
    }

    /**
     * Checks whether this service is terminated.
     *
     * @return true if the FSM is terminated (in a terminal state).
     */
    public boolean terminated() {
        return withReadLock(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return terminalStates.contains(currentState);
            }
        });
    }

    /**
     * Checks whether this service is currently terminating (transitioning to a terminal state).
     *
     * @return true if the FSM is terminating.
     */
    public boolean isTerminating() {
        return withReadLock(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                if(currentEvent == null) {
                    return false;
                }
                final TransitionDescriptor<S, E> descriptor = transitionsTable.get(currentState, currentEvent);
                return terminalStates.contains(descriptor.getTarget());
            }
        });
    }

    /**
     * Shutdowns and cleans up the service.
     *
     * @throws IllegalStateException if service has not terminated yet.
     * @see #terminated()
     */
    public void shutdown() {
        withReadLock(new Runnable() {

            @Override
            public void run() {
                checkState(terminated(), "Service has not terminated yet. Current state: %s.", getCurrentState());
            }
        });
        internalShutdown();
        try {
            service.awaitTermination(2, TimeUnit.SECONDS);
            log.info("Service has been shut down properly.");
        } catch(final InterruptedException e) {
            log.warn("Interrupted during shutdown.");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Fires a specific event and waits synchronously for a completion of the transition. It is useful mostly for tests
     * so it is package-protected.
     *
     * @param event an event to proceed with.
     */
    void fireAndWaitForTransitionToComplete(final E event) {
        log.debug("{} fired. I will be waiting for the transition to complete.", event);
        final EventHolder holder = new EventHolder(event);
        eventQueue.add(holder);
        holder.getSemaphore().acquireUninterruptibly();
        log.debug("{} transition completed.", event);
    }

    /**
     * Fires a specific event and waits synchronously for a completion of the transition. It is useful mostly for tests
     * so it is package-protected.
     *
     * @param event an event to proceed with.
     */
    void fireAndWaitForStableState(final E event) {
        log.debug("{} fired. I will be waiting for the stable state.", event);
        final EventHolder holder = new EventHolder(event);
        eventQueue.add(holder);
        holder.getSemaphore().acquireUninterruptibly();

        boolean stable = false;
        while(!stable) {
            stable = withReadLock(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    return eventQueue.isEmpty() && currentEvent == null;
                }
            });
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
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

    @Override
    public String toString() {
        return withReadLock(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return toStringHelper(this).addValue(currentState).addValue(currentEvent).toString();
            }
        });
    }

    public void setEventBus(final org.hage.bus.EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void drainEvents() {
        for(EventHolder holder : consumingIterable(eventQueue)) {
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
            withWriteLock(new Runnable() {

                @Override
                public void run() {
                    setCurrentState(descriptor.getTarget());
                    setCurrentEvent(null);
                }
            });

            try {
                eventBus.post(eventCreate.invoke(null, descriptor.getInitial(), descriptor.getEvent(),
                                                 descriptor.getTarget()));
            } catch(IllegalAccessException | InvocationTargetException e) {
                log.error("Cannot create prover event object.", e);
            }
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
            if(terminated()) {
                if(!eventQueue.isEmpty()) {
                    log.warn("Service already terminated ({}).", currentState);
                    drainEvents();
                }

                if(!service.isShutdown() && shutdownAfterTerminalState) {
                    internalShutdown();
                }
                return;
            }

            assert currentEvent == null : "There is an event still running.";

            try {
                while(eventQueue.isEmpty()) {
                    Thread.sleep(10);
                }
            } catch(final InterruptedException e) {
                log.debug("Interrupted when waiting for event. Returning.", e);
                return;
            }

            final EventHolder holder;
            final E event;
            try {
                stateLock.writeLock().lock();
                try {
                    holder = eventQueue.take();
                } catch(final InterruptedException e) {
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
            if(transitionDescriptor.isNull()) {
                log.debug("Null transition.");
                fire(failureEvent);
                setCurrentEvent(null);
                holder.getSemaphore().release();
                return;
            }
            log.debug("Planned transition: {}.", transitionDescriptor);

            final Runnable runnable = transitionDescriptor.getAction();
            if(runnable instanceof CallableAdapter) {
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
            if(failureEvent.equals(this.event) && failureEvent.equals(o.event)) {
                return 0;
            } else if(failureEvent.equals(this.event) && !failureEvent.equals(o.event)) {
                return 1;
            } else if(!failureEvent.equals(this.event) && !failureEvent.equals(o.event)) {
                return -1;
            }
            return 0;
        }
    }

}
