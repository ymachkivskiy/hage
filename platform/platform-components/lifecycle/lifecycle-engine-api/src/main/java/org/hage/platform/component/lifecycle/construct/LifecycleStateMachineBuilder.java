package org.hage.platform.component.lifecycle.construct;


import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;
import org.hage.platform.component.lifecycle.LifecycleStateMachine;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.EnumSet.allOf;


@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Getter(AccessLevel.PACKAGE)
@Slf4j
public class LifecycleStateMachineBuilder {

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private EventBus eventBus;

    private final Table<LifecycleState, LifecycleEvent, Class<? extends LifecycleAction>> actions;
    private final Table<LifecycleState, LifecycleEvent, LifecycleState> transitions;
    private final Map<LifecycleEvent, Class<? extends LifecycleAction>> noStateActions;
    private final Map<LifecycleEvent, LifecycleState> noStateTransitions;

    private LifecycleState initialState;
    private Set<LifecycleState> terminalStates = emptySet();

    private final FailureBehaviorBuilder failureBehaviorBuilder;
    private boolean shutdownWhenTerminated = false;

    private LifecycleStateMachineBuilder() {
        transitions = ArrayTable.create(allOf(LifecycleState.class), allOf(LifecycleEvent.class));
        actions = ArrayTable.create(allOf(LifecycleState.class), allOf(LifecycleEvent.class));
        noStateTransitions = new EnumMap<>(LifecycleEvent.class);
        noStateActions = new EnumMap<>(LifecycleEvent.class);
        failureBehaviorBuilder = new FailureBehaviorBuilder(this);
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

    public LifecycleStateMachineBuilder shutdownServiceWhenTerminated() {
        shutdownWhenTerminated = true;
        return this;
    }

    public FailureBehaviorBuilder ifFailed() {
        return failureBehaviorBuilder;
    }

    public LifecycleStateMachine build() {
        log.debug("Building a state machine.");

        checkState(initialState != null);
        checkState(failureBehaviorBuilder.getEvent() != null);

        return new LifecycleStateMachineService(this);
    }

    TransitionDescriptor transitionFor(LifecycleState state, LifecycleEvent event) {
        final LifecycleState target;
        final Class<? extends LifecycleAction> actionClass;

        // More specific declarations are preferred
        if (transitions.get(state, event) != null) {
            target = transitions.get(state, event);
            actionClass = actions.get(state, event);
        } else if (noStateTransitions.get(event) != null) {
            target = noStateTransitions.get(event);
            actionClass = noStateActions.get(event);
        } else {
            return TransitionDescriptor.nullTransition();
        }

        TransitionDescriptor descriptor = TransitionDescriptor.transition(state, target, event, beanFactory.getBean(actionClass));

        log.debug("New transition: {}.", descriptor);

        return descriptor;
    }

}
