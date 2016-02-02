package org.hage.platform.component.lifecycle.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AnyStateActionBuilder {

    private final LifecycleStateMachineBuilder builder;

    private LifecycleEvent event;
    private LifecycleState exit;
    private Class<? extends LifecycleAction> action = EmptyLifecycleAction.class;

    public AnyStateActionBuilder on(LifecycleEvent initiatingEvent) {
        this.event = initiatingEvent;
        return this;
    }

    public AnyStateActionBuilder execute(Class<? extends LifecycleAction> actionToExecute) {
        this.action = actionToExecute;
        return this;
    }

    public AnyStateActionBuilder goTo(LifecycleState state) {
        this.exit = state;
        return this;
    }

    public AnyStateActionBuilder and() {
        builder.getNoStateTransitions().put(event, exit);
        builder.getNoStateActions().put(event, action);
        return this;
    }

    public LifecycleStateMachineBuilder commit() {
        builder.getNoStateTransitions().put(event, exit);
        builder.getNoStateActions().put(event, action);
        return builder;
    }
}
