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
                .on(LifecycleEvent.CORE_STARTING)
                    .goTo(RUNNING)
                .and()
                .on(PAUSE_FOR_RE_BALANCE)
                    .execute(PauseLifecycleAction.class)
                    .goTo(PAUSED)
                .and()
                .on(STOP_SIMULATION)
                    .execute(StopLifecycleAction.class)
                    .goTo(STOPPED)
                .commit()

            .in(PAUSED)
                .on(RESUME_SIMULATION)
                    .execute(ResumeLifecycleAction.class)
                    .goTo(RUNNING)
                .commit()

            .in(STOPPED)
                .on(LifecycleEvent.CORE_STOPPED)
                    .execute(CoreStoppedLifecycleAction.class)
                    .goTo(STOPPED)
                .and()
                .on(CLEAR_SIMULATION_CONFIGURATION)
                    .execute(ClearLifecycleAction.class)
                    .goTo(INITIALIZED)
                .commit()

            .inAnyState()
                .on(LifecycleEvent.EXIT)
                    .execute(ExitLifecycleAction.class)
                    .goTo(TERMINATED)
                .and()
                .on(LifecycleEvent.ERROR)
                    .execute(ErrorLifecycleAction.class)
                    .goTo(LifecycleState.FAILED)
                .commit()

            .ifFailed().fire(LifecycleEvent.ERROR)

            .shutdownServiceWhenTerminated();

        // @formatter:on

    }

    @Override
    public LifecycleEvent getStartingEvent() {
        return PERFORM_CLUSTER_INITIALIZATION;
    }
}
