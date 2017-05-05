package org.hage.platform.node.lifecycle;

import org.hage.platform.node.lifecycle.action.*;
import org.hage.platform.node.lifecycle.construct.LifecycleStateMachineBuilder;

import static org.hage.platform.node.lifecycle.LifecycleEvent.*;

public class DefaultLifecycleInitializer implements LifecycleInitializer {

    @Override
    public void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder) {

        // @formatter:off

        lifecycleBuilder

            .startWith(LifecycleState.OFFLINE)
            .terminateIn(LifecycleState.TERMINATED, LifecycleState.FAILED)

            .in(LifecycleState.OFFLINE)
                .on(PERFORM_CLUSTER_INITIALIZATION)
                    .execute(InitializationLifecycleAction.class)
                    .goTo(LifecycleState.INITIALIZED)
                .commit()

            .in(LifecycleState.INITIALIZED)
                .on(CONFIGURE)
                    .execute(ConfigurationLifecycleAction.class)
                    .goTo(LifecycleState.CONFIGURED)
                .commit()

            .in(LifecycleState.CONFIGURED)
                .on(START_SIMULATION)
                    .execute(StartLifecycleAction.class)
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .in(LifecycleState.RUNNING)
                .on(PAUSE_FOR_RE_BALANCE)
                    .execute(ReBalanceLifecycleAction.class)
                    .goTo(LifecycleState.RE_BALANCING)
                .and()
                .on(STOP_SIMULATION)
                    .execute(StopLifecycleAction.class)
                    .goTo(LifecycleState.STOPPED)
                .commit()

            .in(LifecycleState.RE_BALANCING)
                .on(RESUME_SIMULATION)
                    .execute(ResumeAfterReBalanceLifecycleAction.class)
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .inAnyState()
                .on(EXIT)
                    .execute(ExitLifecycleAction.class)
                    .goTo(LifecycleState.TERMINATED)
                .and()
                .on(ERROR)
                    .execute(ErrorLifecycleAction.class)
                    .goTo(LifecycleState.FAILED)
                .commit()

            .ifFailed().fire(ERROR)

            .shutdownServiceWhenTerminated();

        // @formatter:on

    }

    @Override
    public LifecycleEvent getStartingEvent() {
        return PERFORM_CLUSTER_INITIALIZATION;
    }
}
