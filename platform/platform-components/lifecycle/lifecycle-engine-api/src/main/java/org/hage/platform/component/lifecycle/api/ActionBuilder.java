package org.hage.platform.component.lifecycle.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ActionBuilder {

    private final LifecycleStateMachineBuilder builder;
    private final LifecycleState entry;

    private LifecycleEvent event;
    private LifecycleState exit;
    private Class<? extends LifecycleAction> actionClass = EmptyLifecycleAction.class;

    public ActionBuilder on(LifecycleEvent initiatingEvent) {
        this.event = initiatingEvent;
        return this;
    }

    public ActionBuilder execute(Class<? extends LifecycleAction> actionToExecute) {
        this.actionClass = actionToExecute;
        return this;
    }

    public ActionBuilder goTo(LifecycleState state) {
        this.exit = state;
        return this;
    }

    public ActionBuilder and() {
        builder.getTransitions().put(entry, event, exit);
        builder.getActions().put(entry, event, actionClass);
        return this;
    }

    public LifecycleStateMachineBuilder commit() {
        builder.getTransitions().put(entry, event, exit);
        builder.getActions().put(entry, event, actionClass);
        return builder;
    }
}
