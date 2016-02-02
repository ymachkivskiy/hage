package org.hage.platform.component.lifecycle.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.LifecycleEvent;

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
