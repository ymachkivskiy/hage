package org.hage.platform.component.lifecycle.api;


import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;
import org.hage.platform.component.lifecycle.LifecycleStateMachine;
import org.hage.platform.util.bus.EventBus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.EnumSet.allOf;


@Getter(AccessLevel.PACKAGE)
@Slf4j
public class LifecycleStateMachineBuilder {

    private final Table<LifecycleState, LifecycleEvent, Action> actions;
    private final Table<LifecycleState, LifecycleEvent, LifecycleState> transitions;
    private final Map<LifecycleEvent, Action> noStateActions;
    private final Map<LifecycleEvent, LifecycleState> noStateTransitions;

    private LifecycleState initialState;
    private Set<LifecycleState> terminalStates = emptySet();

    private final FailureBehaviorBuilder failureBehaviorBuilder;

    private boolean shutdownWhenTerminated = false;
    private EventBus eventBus;

    LifecycleStateMachineBuilder() {
        transitions = ArrayTable.create(allOf(LifecycleState.class), allOf(LifecycleEvent.class));
        actions = ArrayTable.create(allOf(LifecycleState.class), allOf(LifecycleEvent.class));
        noStateTransitions = new EnumMap<>(LifecycleEvent.class);
        noStateActions = new EnumMap<>(LifecycleEvent.class);
        failureBehaviorBuilder = new FailureBehaviorBuilder(this);
    }

    LifecycleStateMachineBuilder withEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    public ActionBuilder in(LifecycleState state) {
        return new ActionBuilder(this, state);
    }

    public AnyStateActionBuilder inAnyState() {
        return new AnyStateActionBuilder(this);
    }

    public LifecycleStateMachineBuilder startWith(LifecycleState state) {
        initialState = state;
        return this;
    }

    public LifecycleStateMachineBuilder terminateIn(LifecycleState... states) {
        terminalStates = EnumSet.copyOf(asList(states));
        return this;
    }

    public LifecycleStateMachineBuilder shutdownWhenTerminated() {
        shutdownWhenTerminated = true;
        return this;
    }

    public FailureBehaviorBuilder ifFailed() {
        return failureBehaviorBuilder;
    }

    LifecycleStateMachine build() {
        log.debug("Building a state machine.");

        checkState(initialState != null);
        checkState(failureBehaviorBuilder.getEvent() != null);
        checkState(eventBus != null); //// TODO: 01.02.16 remove this by doing bus optional

        return new LifecycleStateMachineService(this);
    }

    public TransitionDescriptor transitionFor(LifecycleState state, LifecycleEvent event) {
        final LifecycleState target;
        final Action action;

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

}
