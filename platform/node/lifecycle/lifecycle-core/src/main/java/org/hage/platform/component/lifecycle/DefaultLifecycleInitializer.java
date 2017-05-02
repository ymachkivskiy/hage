package org.hage.platform.component.lifecycle;

import org.hage.platform.component.lifecycle.action.*;
import org.hage.platform.component.lifecycle.construct.LifecycleStateMachineBuilder;

import static org.hage.platform.component.lifecycle.LifecycleEvent.*;
import static org.hage.platform.component.lifecycle.LifecycleState.*;

public class DefaultLifecycleInitializer implements LifecycleInitializer {

    @Override
    public void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder) {

        // @formatter:off

        lifecycleBuilder

            .startWith(OFFLINE)
            .terminateIn(TERMINATED, FAILED)

            .in(OFFLINE)
                .on(PERFORM_CLUSTER_INITIALIZATION)
                    .execute(InitializationLifecycleAction.class)
                    .goTo(INITIALIZED)
                .commit()

            .in(INITIALIZED)
                .on(CONFIGURE)
                    .execute(ConfigurationLifecycleAction.class)
                    .goTo(CONFIGURED)
                .commit()

            .in(CONFIGURED)
                .on(START_SIMULATION)
                    .execute(StartLifecycleAction.class)
                    .goTo(RUNNING)
                .commit()

            .in(RUNNING)
                .on(PAUSE_FOR_RE_BALANCE)
                    .execute(ReBalanceLifecycleAction.class)
                    .goTo(RE_BALANCING)
                .and()
                .on(STOP_SIMULATION)
                    .execute(StopLifecycleAction.class)
                    .goTo(STOPPED)
                .commit()

            .in(RE_BALANCING)
                .on(RESUME_SIMULATION)
                    .execute(ResumeAfterReBalanceLifecycleAction.class)
                    .goTo(RUNNING)
                .commit()

            .inAnyState()
                .on(EXIT)
                    .execute(ExitLifecycleAction.class)
                    .goTo(TERMINATED)
                .and()
                .on(ERROR)
                    .execute(ErrorLifecycleAction.class)
                    .goTo(FAILED)
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
