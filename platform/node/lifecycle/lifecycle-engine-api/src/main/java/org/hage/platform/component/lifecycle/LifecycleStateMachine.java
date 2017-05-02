package org.hage.platform.component.lifecycle;

public interface LifecycleStateMachine {

    void fire(LifecycleEvent event);

    void fire(final LifecycleEvent event, final Object parameter);

    void fireAndWaitForTransitionToComplete(final LifecycleEvent event);

    boolean terminated();

    boolean isTerminating();

}
