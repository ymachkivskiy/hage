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

package org.jage.platform.fsm;


import com.google.common.collect.ArrayTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;
import org.jage.annotation.ReturnValuesAreNonnullByDefault;
import org.jage.bus.EventBus;
import org.jage.exception.AgeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Maps.newEnumMap;


/**
 * A builder of {@link StateMachineService} instances. It offers a simple, flexible interface for creation of state
 * machines.
 * <p>
 * <p>
 * Initially, a user is required to provide at least:
 * <ul>
 * <li> an enumeration of states,
 * <li> an enumeration of transitions,
 * <li> an entry state ({@link #startWith}),
 * <li> terminal states ({@link #terminateIn}),
 * <li> an event to fire on failures and errors ({@link #ifFailed}).
 * </ul>
 * Failure to do so results in {@link IllegalStateException} when {@link #build} is called.
 *
 * @param <S> the states enumeration.
 * @param <E> the events enumeration.
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
public class StateMachineServiceBuilder<S extends Enum<S>, E extends Enum<E>> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineServiceBuilder.class);
    @Nonnull
    private final FailureBehaviorBuilder failureBehaviorBuilder = new FailureBehaviorBuilder();
    private Table<S, E, S> transitions;
    private Map<E, S> noStateTransitions;
    private Table<S, E, Runnable> actions;
    private Map<E, Runnable> noStateActions;
    private Class<S> stateClass;
    private Class<E> eventClass;
    private S initialState;
    private EnumSet<S> terminalStates;
    private boolean synchronousNotifications = false;

    private boolean shutdownWhenTerminated = false;

    private Class<? extends StateChangedEvent> stateChangedEventClass = StateChangedEvent.class;

    /**
     * Creates a new builder instance.
     *
     * @param <V> the states enumeration.
     * @param <T> the events enumeration.
     * @return a builder instance.
     */
    public static <V extends Enum<V>, T extends Enum<T>> StateMachineServiceBuilder<V, T> create() {
        return new StateMachineServiceBuilder<>();
    }

    /**
     * Declares the enumeration with FSM states.
     *
     * @param states an enumeration with states.
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> states(final Class<S> states) {
        stateClass = states;
        initializeTables();
        return this;
    }

    private void initializeTables() {
        if(stateClass != null && eventClass != null) {
            transitions = ArrayTable.create(EnumSet.allOf(stateClass), EnumSet.allOf(eventClass));
            actions = ArrayTable.create(EnumSet.allOf(stateClass), EnumSet.allOf(eventClass));
        }
        if(eventClass != null) {
            noStateTransitions = newEnumMap(eventClass);
            noStateActions = newEnumMap(eventClass);
        }
    }

    /**
     * Declares the enumeration with FSM events.
     *
     * @param events an enumeration with events.
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> events(final Class<E> events) {
        eventClass = events;
        initializeTables();
        return this;
    }

    /**
     * Requests that events will be sent synchronously.
     *
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> notifySynchronously() {
        synchronousNotifications = true;
        return this;
    }

    /**
     * Requests that events will be sent synchronously.
     *
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> notifyWithType(Class<? extends StateChangedEvent> stateChangedEventClass) {
        this.stateChangedEventClass = stateChangedEventClass;
        return this;
    }

    /**
     * Starts the declaration of behaviour when the FSM is at the given state.
     *
     * @param state a state.
     * @return an action builder.
     */
    public ActionBuilder in(final S state) {
        checkState(stateClass != null);
        checkState(eventClass != null);

        return new ActionBuilder(state);
    }

    /**
     * Starts the declaration of behaviour for the events that are not dependent on states.
     *
     * @return an action builder.
     */
    public AnyStateActionBuilder inAnyState() {
        checkState(eventClass != null);

        return new AnyStateActionBuilder();
    }

    /**
     * Declares an initialState state.
     *
     * @param state a state.
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> startWith(final S state) {
        initialState = state;
        return this;
    }

    /**
     * Indicates which states are terminalStates.
     *
     * @param states states that should be marked as terminalStates.
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> terminateIn(final S... states) {
        terminalStates = EnumSet.copyOf(Arrays.asList(states));
        return this;
    }

    /**
     * Starts the declaration of actions taken when the failure occurs.
     *
     * @return a failure behaviour builder.
     */
    public FailureBehaviorBuilder ifFailed() {
        return failureBehaviorBuilder;
    }

    /**
     * Schedules FSM shutdown after a terminalStates state has been reached.
     *
     * @return this builder instance.
     */
    public StateMachineServiceBuilder<S, E> shutdownWhenTerminated() {
        shutdownWhenTerminated = true;
        return this;
    }

    // Package-protected methods for service creation and testing

    /**
     * Builds and returns a new service.
     *
     * @return a new {@code StateMachineService}.
     */
    public StateMachineService<S, E> build() {
        log.debug("Building a state machine: S={}, E={}.", stateClass, eventClass);
        checkState(stateClass != null);
        checkState(eventClass != null);
        checkState(initialState != null);
        checkState(terminalStates != null);
        checkState(failureBehaviorBuilder.getEvent() != null);

        try {
            return new StateMachineService<S, E>(this) {
                /* Empty */
            };
        } catch(NoSuchMethodException e) {
            log.error("Incorrect event class.", e);
            throw new AgeException(e);
        }
    }

    @Nullable
    Table<S, E, S> getTransitions() {
        return transitions;
    }

    @Nullable
    Table<S, E, Runnable> getActions() {
        return actions;
    }

    @Nullable
    S getInitialState() {
        return initialState;
    }

    @Nonnull
    FailureBehaviorBuilder getFailureBehaviorBuilder() {
        return failureBehaviorBuilder;
    }

    EnumSet<S> getTerminalStates() {
        return terminalStates;
    }

    EventBus getEventBus() {
        return new EventBus("default", this.synchronousNotifications);
    }

    boolean getShutdownWhenTerminated() {
        return shutdownWhenTerminated;
    }

    @Nullable
    Map<E, S> getAnyTransitions() {
        return noStateTransitions;
    }

    @Nullable
    Map<E, Runnable> getAnyActions() {
        return noStateActions;
    }

    Class<? extends StateChangedEvent> getStateChangedEventClass() {
        return stateChangedEventClass;
    }

    /**
     * Builds the transitions table.
     *
     * @return an immutable transitions table.
     */
    ImmutableTable<S, E, TransitionDescriptor<S, E>> buildTransitionsTable() {
        final EnumSet<S> allStates = EnumSet.allOf(getStateClass());
        final EnumSet<E> allEvents = EnumSet.allOf(getEventClass());
        final Builder<S, E, TransitionDescriptor<S, E>> tableBuilder = ImmutableTable.builder();
        for(final S state : allStates) {
            for(final E event : allEvents) {
                tableBuilder.put(state, event, transitionFor(state, event));
            }
        }
        return tableBuilder.build();
    }

    @Nullable
    Class<S> getStateClass() {
        return stateClass;
    }

    @Nullable
    Class<E> getEventClass() {
        return eventClass;
    }

    private TransitionDescriptor<S, E> transitionFor(final S state, final E event) {
        final S target;
        final Runnable action;

        // More specific declarations are preferred
        if(transitions.get(state, event) != null) {
            target = transitions.get(state, event);
            action = actions.get(state, event);
        } else if(noStateTransitions.get(event) != null) {
            target = noStateTransitions.get(event);
            action = noStateActions.get(event);
        } else {
            return TransitionDescriptor.getNull();
        }

        final TransitionDescriptor<S, E> descriptor = new TransitionDescriptor<>(state, event, target, action);
        log.debug("New transition: {}.", descriptor);
        return descriptor;
    }


    /**
     * An action builder.
     *
     * @author AGH AgE Team
     */
    @ReturnValuesAreNonnullByDefault
    public final class ActionBuilder {

        private final S entry;

        private E event;

        private S exit;

        private Runnable action;

        private ActionBuilder(final S entry) {
            this.entry = entry;
        }

        /**
         * Declares an event that causes the action.
         *
         * @param initiatingEvent a causing event.
         * @return this action builder.
         */
        public ActionBuilder on(final E initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        /**
         * Declares an action to be executed during transition.
         *
         * @param actionToExecute an action to execute.
         * @return this action builder.
         */
        public ActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        /**
         * Declares an action to be executed during transition.
         *
         * @param <T>             a type returned by the action.
         * @param actionToExecute an action to execute.
         * @return this action builder.
         */
        public <T> ActionBuilder execute(final CallableWithParameters<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        /**
         * Declares a target state.
         *
         * @param state a target state.
         * @return this action builder.
         */
        public ActionBuilder goTo(final S state) {
            this.exit = state;
            return this;
        }

        /**
         * Starts a new action declaration for the current state.
         *
         * @return this action builder.
         */
        public ActionBuilder and() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return this;
        }

        /**
         * Finishes the action declaration.
         *
         * @return a state machine builder.
         */
        public StateMachineServiceBuilder<S, E> commit() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return StateMachineServiceBuilder.this;
        }
    }


    /**
     * An action builder for state-independent actions.
     *
     * @author AGH AgE Team
     */
    @ReturnValuesAreNonnullByDefault
    public class AnyStateActionBuilder {

        private E event;

        private S exit;

        private Runnable action;

        /**
         * Declares an event that causes the action.
         *
         * @param initiatingEvent a causing event.
         * @return this action builder.
         */
        public AnyStateActionBuilder on(final E initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        /**
         * Declares an action to be executed during transition.
         *
         * @param actionToExecute an action to execute.
         * @return this action builder.
         */
        public AnyStateActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        /**
         * Declares an action to be executed during transition.
         *
         * @param <T>             a type returned by the action.
         * @param actionToExecute an action to execute.
         * @return this action builder.
         */
        public <T> AnyStateActionBuilder execute(final CallableWithParameters<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        /**
         * Declares a target state.
         *
         * @param state a target state.
         * @return this action builder.
         */
        public AnyStateActionBuilder goTo(final S state) {
            this.exit = state;
            return this;
        }

        /**
         * Starts a new action declaration for the current state.
         *
         * @return this action builder.
         */
        public AnyStateActionBuilder and() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return this;
        }

        /**
         * Finishes the action declaration.
         *
         * @return a state machine builder.
         */
        public StateMachineServiceBuilder<S, E> commit() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return StateMachineServiceBuilder.this;
        }
    }


    /**
     * A builder for internal FSM failure.
     *
     * @author AGH AgE Team
     */
    public class FailureBehaviorBuilder {

        @Nullable
        private E event;

        /**
         * Declares which event should be fired when failure occurs.
         *
         * @param eventToFire an event to fire.
         * @return a state machine builder.
         */
        public StateMachineServiceBuilder<S, E> fire(final E eventToFire) {
            this.event = eventToFire;
            return StateMachineServiceBuilder.this;
        }

        @Nullable
        E getEvent() {
            return event;
        }
    }

}
