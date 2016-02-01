package org.hage.platform.component.lifecycle;


import com.google.common.collect.ArrayTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.event.LifecycleEvent;
import org.hage.platform.component.lifecycle.event.LifecycleState;
import org.hage.platform.util.bus.EventBus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;


@Getter(AccessLevel.PACKAGE)
@Slf4j
public class LifecycleEngineServiceBuilder {

    private final Table<LifecycleState, LifecycleEvent, Runnable> actions;
    private final Table<LifecycleState, LifecycleEvent, LifecycleState> transitions;
    private final Map<LifecycleEvent, Runnable> noStateActions;
    private final Map<LifecycleEvent, LifecycleState> noStateTransitions;
    private final FailureBehaviorBuilder failureBehaviorBuilder;

    private LifecycleState initialState;
    private Set<LifecycleState> terminalStates = emptySet();

    private boolean shutdownWhenTerminated = false;

    private EventBus eventBus;

    public static LifecycleEngineServiceBuilder engineBuilder() {
        return new LifecycleEngineServiceBuilder();
    }

    public LifecycleEngineServiceBuilder() {

        transitions = ArrayTable.create(EnumSet.allOf(LifecycleState.class), EnumSet.allOf(LifecycleEvent.class));
        actions = ArrayTable.create(EnumSet.allOf(LifecycleState.class), EnumSet.allOf(LifecycleEvent.class));

        noStateTransitions = new EnumMap<>(LifecycleEvent.class);
        noStateActions = new EnumMap<>(LifecycleEvent.class);

        failureBehaviorBuilder = new FailureBehaviorBuilder();
    }

    public LifecycleEngineServiceBuilder withEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    public ActionBuilder in(final LifecycleState state) {
        return new ActionBuilder(state);
    }

    public AnyStateActionBuilder inAnyState() {
        return new AnyStateActionBuilder();
    }

    public LifecycleEngineServiceBuilder startWith(LifecycleState state) {
        initialState = state;
        return this;
    }

    public LifecycleEngineServiceBuilder terminateIn(final LifecycleState... states) {
        terminalStates = EnumSet.copyOf(asList(states));
        return this;
    }

    public LifecycleEngineServiceBuilder shutdownWhenTerminated() {
        shutdownWhenTerminated = true;
        return this;
    }

    public FailureBehaviorBuilder ifFailed() {
        return failureBehaviorBuilder;
    }

    public LifecycleEngineService build() {
        log.debug("Building a state machine.");

        checkState(initialState != null);
        checkState(failureBehaviorBuilder.getEvent() != null);
        checkState(eventBus != null); //// TODO: 01.02.16 remove this by doing bus optional

        return new LifecycleEngineService(this);
    }

    ImmutableTable<LifecycleState, LifecycleEvent, TransitionDescriptor> buildTransitionsTable() {
        final EnumSet<LifecycleState> allStates = EnumSet.allOf(LifecycleState.class);
        final EnumSet<LifecycleEvent> allEvents = EnumSet.allOf(LifecycleEvent.class);

        final Builder<LifecycleState, LifecycleEvent, TransitionDescriptor> tableBuilder = ImmutableTable.builder();

        for (final LifecycleState state : allStates) {
            for (final LifecycleEvent event : allEvents) {
                tableBuilder.put(state, event, transitionFor(state, event));
            }
        }

        return tableBuilder.build();
    }

    private TransitionDescriptor transitionFor(final LifecycleState state, final LifecycleEvent event) {
        final LifecycleState target;
        final Runnable action;

        // More specific declarations are preferred
        if (transitions.get(state, event) != null) {
            target = transitions.get(state, event);
            action = actions.get(state, event);
        } else if (noStateTransitions.get(event) != null) {
            target = noStateTransitions.get(event);
            action = noStateActions.get(event);
        } else {
            return TransitionDescriptor.nullTransition();
        }

        final TransitionDescriptor descriptor = TransitionDescriptor.transition(state, target, event, action);
        log.debug("New transition: {}.", descriptor);
        return descriptor;
    }

    @RequiredArgsConstructor
    public final class ActionBuilder {

        private final LifecycleState entry;
        private LifecycleEvent event;
        private LifecycleState exit;
        private Runnable action;

        public ActionBuilder on(final LifecycleEvent initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        public ActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        public <T> ActionBuilder execute(final CallableWithParameter<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        public ActionBuilder goTo(final LifecycleState state) {
            this.exit = state;
            return this;
        }

        public ActionBuilder and() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return this;
        }

        public LifecycleEngineServiceBuilder commit() {
            transitions.put(entry, event, exit);
            actions.put(entry, event, action);
            return LifecycleEngineServiceBuilder.this;
        }
    }

    public class AnyStateActionBuilder {

        private LifecycleEvent event;
        private LifecycleState exit;
        private Runnable action;

        public AnyStateActionBuilder on(final LifecycleEvent initiatingEvent) {
            this.event = initiatingEvent;
            return this;
        }

        public AnyStateActionBuilder execute(final Runnable actionToExecute) {
            this.action = actionToExecute;
            return this;
        }

        public <T> AnyStateActionBuilder execute(final CallableWithParameter<T> actionToExecute) {
            this.action = new CallableAdapter<>(actionToExecute);
            return this;
        }

        public AnyStateActionBuilder goTo(final LifecycleState state) {
            this.exit = state;
            return this;
        }

        public AnyStateActionBuilder and() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return this;
        }

        public LifecycleEngineServiceBuilder commit() {
            noStateTransitions.put(event, exit);
            noStateActions.put(event, action);
            return LifecycleEngineServiceBuilder.this;
        }
    }

    public class FailureBehaviorBuilder {

        @Getter
        private LifecycleEvent event;

        public LifecycleEngineServiceBuilder fire(final LifecycleEvent eventToFire) {
            this.event = eventToFire;
            return LifecycleEngineServiceBuilder.this;
        }
    }

}
