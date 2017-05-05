package org.hage.platform.node.lifecycle.construct;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hage.platform.node.lifecycle.LifecycleAction;
import org.hage.platform.node.lifecycle.LifecycleEvent;
import org.hage.platform.node.lifecycle.LifecycleState;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ActionBuilder {

    private final LifecycleStateMachineBuilder builder;
    private final LifecycleState entry;

    private LifecycleEvent event;
    private LifecycleState exit;
    private Class<? extends LifecycleAction> actionClass;

    {
        reset();
    }

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
        reset();
        return this;
    }

    public LifecycleStateMachineBuilder commit() {
        builder.getTransitions().put(entry, event, exit);
        builder.getActions().put(entry, event, actionClass);
        return builder;
    }

    private void reset() {
        actionClass = EmptyLifecycleAction.class;
    }
}
