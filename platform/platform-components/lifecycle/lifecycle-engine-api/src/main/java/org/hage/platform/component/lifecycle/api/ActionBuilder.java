package org.hage.platform.component.lifecycle.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ActionBuilder {

    private final LifecycleStateMachineBuilder builder;
    private final LifecycleState entry;
    private LifecycleEvent event;
    private LifecycleState exit;
    private Action action = EmptyAction.INSTANCE;

    public ActionBuilder on(LifecycleEvent initiatingEvent) {
        this.event = initiatingEvent;
        return this;
    }

    public ActionBuilder execute(Action actionToExecute) {
        this.action = actionToExecute;
        return this;
    }

    public ActionBuilder goTo(LifecycleState state) {
        this.exit = state;
        return this;
    }

    public ActionBuilder and() {
        builder.getTransitions().put(entry, event, exit);
        builder.getActions().put(entry, event, action);
        return this;
    }

    public LifecycleStateMachineBuilder commit() {
        builder.getTransitions().put(entry, event, exit);
        builder.getActions().put(entry, event, action);
        return builder;
    }
}
