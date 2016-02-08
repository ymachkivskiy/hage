package org.hage.platform.component.lifecycle;

import org.hage.platform.component.lifecycle.action.*;
import org.hage.platform.component.lifecycle.construct.LifecycleStateMachineBuilder;

public class DefaultLifecycleInitializer implements LifecycleInitializer {

    @Override
    public void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder) {

        // @formatter:off

        lifecycleBuilder

            .startWith(LifecycleState.OFFLINE)
            .terminateIn(LifecycleState.TERMINATED)

            .in(LifecycleState.OFFLINE)
                .on(LifecycleEvent.INITIALIZE)
                    .execute(InitializationLifecycleAction.class)
                    .goTo(LifecycleState.INITIALIZED)
                .commit()

            .in(LifecycleState.INITIALIZED)
                .on(LifecycleEvent.CONFIGURE)
                    .execute(ConfigurationLifecycleAction.class)
                    .goTo(LifecycleState.CONFIGURED)
                .commit()

            .in(LifecycleState.CONFIGURED)
                .on(LifecycleEvent.START_COMMAND)
                    .execute(StartLifecycleAction.class)
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .in(LifecycleState.RUNNING)
                .on(LifecycleEvent.CORE_STARTING)
                    .goTo(LifecycleState.RUNNING)
                .and()
                .on(LifecycleEvent.PAUSE)
                    .execute(PauseLifecycleAction.class)
                    .goTo(LifecycleState.PAUSED)
                .and()
                .on(LifecycleEvent.STOP_COMMAND)
                    .execute(StopLifecycleAction.class)
                    .goTo(LifecycleState.STOPPED)
                .commit()

            .in(LifecycleState.PAUSED)
                .on(LifecycleEvent.RESUME)
                    .execute(ResumeLifecycleAction.class)
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .in(LifecycleState.STOPPED)
                .on(LifecycleEvent.CORE_STOPPED)
                    .execute(CoreStoppedLifecycleAction.class)
                    .goTo(LifecycleState.STOPPED)
                .and()
                .on(LifecycleEvent.CLEAR)
                    .execute(ClearLifecycleAction.class)
                    .goTo(LifecycleState.INITIALIZED)
                .commit()

            .inAnyState()
                .on(LifecycleEvent.EXIT)
                    .execute(ExitLifecycleAction.class)
                    .goTo(LifecycleState.TERMINATED)
                .and()
                .on(LifecycleEvent.ERROR)
                    .execute(ErrorLifecycleAction.class)
                    .goTo(LifecycleState.FAILED)
                .commit()

            .ifFailed().fire(LifecycleEvent.ERROR)

            .shutdownWhenTerminated();

        // @formatter:on

        // Register shutdown hook, so we will be able to do a clean shutdown
//        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    @Override
    public LifecycleEvent getStartingEvent() {
        return LifecycleEvent.INITIALIZE;
    }
}
