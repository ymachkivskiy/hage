package org.hage.platform.component.lifecycle;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.action.*;
import org.hage.platform.component.lifecycle.api.BaseLifecycleEngine;
import org.hage.platform.component.lifecycle.api.LifecycleStateMachineBuilder;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.component.services.core.CoreComponentEvent;
import org.hage.platform.component.workplace.StopConditionFulfilledEvent;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;


@Slf4j
public class DefaultLifecycleEngine extends BaseLifecycleEngine implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Override
    protected void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder) {

        // @formatter:off

        lifecycleBuilder

            .startWith(LifecycleState.OFFLINE)
            .terminateIn(LifecycleState.TERMINATED)

            .in(LifecycleState.OFFLINE)
                .on(LifecycleEvent.INITIALIZE)
                    .execute(getActionInstanceByClass(InitializationAction.class))
                    .goTo(LifecycleState.INITIALIZED)
                .commit()

            .in(LifecycleState.INITIALIZED)
                .on(LifecycleEvent.CONFIGURE)
                    .execute(getActionInstanceByClass(ConfigurationAction.class))
                    .goTo(LifecycleState.CONFIGURED)
                .commit()

            .in(LifecycleState.CONFIGURED)
                .on(LifecycleEvent.START_COMMAND)
                    .execute(getActionInstanceByClass(StartAction.class))
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .in(LifecycleState.RUNNING)
                .on(LifecycleEvent.CORE_STARTING)
                    .goTo(LifecycleState.RUNNING)
                .and()
                .on(LifecycleEvent.PAUSE)
                    .execute(getActionInstanceByClass(PauseAction.class))
                    .goTo(LifecycleState.PAUSED)
                .and()
                .on(LifecycleEvent.STOP_COMMAND)
                    .execute(getActionInstanceByClass(StopAction.class))
                    .goTo(LifecycleState.STOPPED)
                .commit()

            .in(LifecycleState.PAUSED)
                .on(LifecycleEvent.RESUME)
                    .execute(getActionInstanceByClass(ResumeAction.class))
                    .goTo(LifecycleState.RUNNING)
                .commit()

            .in(LifecycleState.STOPPED)
                .on(LifecycleEvent.CORE_STOPPED)
                    .execute(getActionInstanceByClass(CoreStoppedAction.class))
                    .goTo(LifecycleState.STOPPED)
                .and()
                .on(LifecycleEvent.CLEAR)
                    .execute(getActionInstanceByClass(ClearAction.class))
                    .goTo(LifecycleState.INITIALIZED)
                .commit()

            .inAnyState()
                .on(LifecycleEvent.EXIT)
                    .execute(getActionInstanceByClass(ExitAction.class))
                    .goTo(LifecycleState.TERMINATED)
                .and()
                .on(LifecycleEvent.ERROR)
                    .execute(getActionInstanceByClass(ErrorAction.class))
                    .goTo(LifecycleState.FAILED)
                .commit()

            .ifFailed().fire(LifecycleEvent.ERROR)

            .shutdownWhenTerminated();

        // @formatter:on

        // Register shutdown hook, so we will be able to do a clean shutdown
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    @Override
    public void performCommand(LifecycleCommand command) {
        switch (command) {
            case FAIL:
                break;
            case NOTIFY:
                break;
            case PAUSE:
                getStateMachine().fire(LifecycleEvent.PAUSE);
                break;
            case START:
                getStateMachine().fire(LifecycleEvent.START_COMMAND);
                break;
            case STOP:
                getStateMachine().fire(LifecycleEvent.STOP_COMMAND);
                break;
            case EXIT:
                getStateMachine().fire(LifecycleEvent.EXIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        getStateMachine().fire(LifecycleEvent.INITIALIZE);
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @Subscribe
        @SuppressWarnings("unused")
        public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
            log.debug("Configuration updated event: {}.", event);
            getStateMachine().fire(LifecycleEvent.CONFIGURE);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onCoreComponentEvent(CoreComponentEvent event) {
            log.debug("Core component Event: {}.", event);
            switch (event.getType()) {
                case CONFIGURED:
                    getStateMachine().fire(LifecycleEvent.START_COMMAND);
                    break;
                case STARTING:
                    getStateMachine().fire(LifecycleEvent.CORE_STARTING);
                    break;
                case STOPPED:
                    getStateMachine().fire(LifecycleEvent.CORE_STOPPED);
                    break;
            }

        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onStopConditionFulfilledEvent(StopConditionFulfilledEvent event) {
            log.debug("Stop condition fulfilled event: {}.", event);
            getStateMachine().fire(LifecycleEvent.STOP_COMMAND);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onExitRequestedEvent(ExitRequestedEvent event) {
            log.debug("Exit requested by event: {}.", event);
            getStateMachine().fire(LifecycleEvent.EXIT);
        }

    }

    // Implementations of actions


    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            log.debug("Shutdown hook called.");
            if (!getStateMachine().terminated() && !getStateMachine().isTerminating()) {
                getStateMachine().fire(LifecycleEvent.EXIT);
                try {
                    Thread.sleep(2000); // Simple wait to let other threads terminate properly.
                } catch (final InterruptedException ignored) {
                    // Ignore
                }
            }
        }
    }

}
