package org.hage.platform.node.lifecycle.construct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.node.lifecycle.LifecycleEvent;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FailureBehaviorBuilder {

    private final LifecycleStateMachineBuilder builder;

    @Getter
    private LifecycleEvent event;

    public LifecycleStateMachineBuilder fire(LifecycleEvent eventToFire) {
        this.event = eventToFire;
        return builder;
    }
}
