package org.hage.platform.component.lifecycle;

import org.hage.platform.component.lifecycle.construct.LifecycleStateMachineBuilder;

public interface LifecycleInitializer {

    void performLifecycleInitialization(LifecycleStateMachineBuilder stateMachineBuilder);

    LifecycleEvent getStartingEvent();

}
