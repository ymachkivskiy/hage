package org.hage.platform.util.fsm;


import com.google.common.collect.ArrayTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;
import org.hage.platform.HageException;
import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.util.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Maps.newEnumMap;


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
    private EventBus eventBus;

    private boolean shutdownWhenTerminated = false;
    private NotificationEventCreator<S, E, ? extends StateChangedEvent<S, E>> notificationCreator;

    public static <V extends Enum<V>, T extends Enum<T>> StateMachineServiceBuilder<V, T> create() {
        return new StateMachineServiceBuilder<>();
    }

    public StateMachineServiceBuilder<S, E> states(final Class<S> states) {
        stateClass = states;
        initializeTables();
        return this;
    }

    private void initializeTables() {
        if (stateClass != null && eventClass != null) {
            transitions = ArrayTable.create(EnumSet.allOf(stateClass), EnumSet.allOf(eventClass));
            actions = ArrayTable.create(EnumSet.allOf(stateClass), EnumSet.allOf(eventClass));
        }
        if (eventClass != null) {
            noStateTransitions = newEnumMap(eventClass);
            noStateActions = newEnumMap(eventClass);
        }
    }

    public StateMachineServiceBuilder<S, E> events(final Class<E> events) {
        eventClass = events;
        initializeTables();
        return this;
    }

    public StateMachineServiceBuilder<S, E> notificationCreator(NotificationEventCreator<S, E, ? extends StateChangedEvent<S, E>> notificationCreator) {
        this.notificationCreator = notificationCreator;
        return this;
    }

    public StateMachineServiceBuilder<S, E> withEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    public ActionBuilder in(final S state) {
        checkState(stateClass != null);
        checkState(eventClass != null);

        return new ActionBuilder(state);
    }

    public AnyStateActionBuilder inAnyState() {
        checkState(eventClass != null);

        return new AnyStateActionBuilder();
    }

    public StateMachineServiceBuilder<S, E> startWith(final S state) {
        initialState = state;
        return this;
    }

    public StateMachineServiceBuilder<S, E> terminateIn(final S... states) {
        terminalStates = EnumSet.copyOf(Arrays.asList(states));
        return this;
    }

    public FailureBehaviorBuilder ifFailed() {
        return failureBehaviorBuilder;
    }

    public StateMachineServiceBuilder<S, E> shutdownWhenTerminated() {
        shutdownWhenTerminated = true;
        return this;
    }

    public StateMachineService<S, E> build() {
        log.debug("Building a state machine: S={}, E={}.", stateClass, eventClass);
        checkState(stateClass != null);
        checkState(eventClass != null);
        checkState(initialState != null);
        checkState(terminalStates != null);
        checkState(failureBehaviorBuilder.getEvent() != null);
        checkState(eventBus != null);

        try {
            return new StateMachineService<S, E>(this) {
                /* Empty */
            };
        } catch (NoSuchMethodException e) {
            log.error("Incorrect event class.", e);
            throw new HageException(e);
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

    public NotificationEventCreator<S, E, ? extends StateChangedEvent<S, E>> getNotificationCreator() {
        return notificationCreator;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    ImmutableTable<S, E, TransitionDescriptor<S, E>> buildTransitionsTable() {
        final EnumSet<S> allStates = EnumSet.allOf(getStateClass());
        final EnumSet<E> allEvents = EnumSet.allOf(getEventClass());
        final Builder<S, E, TransitionDescriptor<S, E>> tableBuilder = ImmutableTable.builder();
        for (final S state : allStates) {
            for (final E event : allEvents) {
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
        if (transitions.get(state, event) != null) {
            target = transitions.get(state, event);
            action = actions.get(state, event);
        } else if (noStateTransitions.get(event) != null) {
            target = noStateTransitions.get(event);
            action = noStateActions.get(event);
        } else {
            return TransitionDescriptor.getNull();
        }

        final TransitionDescriptor<S, E> descriptor = new TransitionDescriptor<>(state, event, target, action);
        log.debug("New transition: {}.", descriptor);
        return descriptor;
    }

    @ReturnValuesAreNonnullByDefault
    public final class ActionBuilder {

        private final S entry;

        private E event;

        private S exit;

        private Runnable action;

        private ActionBuilder(final S entry) {
            this.entry = entry;
        }

        public ActionBuilder on(final E initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        public ActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        public <T> ActionBuilder execute(final CallableWithParameters<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        public ActionBuilder goTo(final S state) {
            this.exit = state;
            return this;
        }

        public ActionBuilder and() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return this;
        }

        public StateMachineServiceBuilder<S, E> commit() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return StateMachineServiceBuilder.this;
        }
    }

    @ReturnValuesAreNonnullByDefault
    public class AnyStateActionBuilder {

        private E event;

        private S exit;

        private Runnable action;

        public AnyStateActionBuilder on(final E initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        public AnyStateActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        public <T> AnyStateActionBuilder execute(final CallableWithParameters<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        public AnyStateActionBuilder goTo(final S state) {
            this.exit = state;
            return this;
        }

        public AnyStateActionBuilder and() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return this;
        }

        public StateMachineServiceBuilder<S, E> commit() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return StateMachineServiceBuilder.this;
        }
    }

    public class FailureBehaviorBuilder {

        @Nullable
        private E event;

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
