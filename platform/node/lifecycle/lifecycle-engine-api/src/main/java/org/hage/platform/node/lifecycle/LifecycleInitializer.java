package org.hage.platform.node.lifecycle;

import org.hage.platform.node.lifecycle.construct.LifecycleStateMachineBuilder;

public interface LifecycleInitializer {

    void performLifecycleInitialization(LifecycleStateMachineBuilder stateMachineBuilder);

    LifecycleEvent getStartingEvent();

}
