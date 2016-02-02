package org.hage.platform.component.lifecycle;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.lifecycle.api.BaseLifecycleEngine;
import org.hage.platform.component.lifecycle.api.LifecycleStateMachineBuilder;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.component.pico.visitor.StatefulComponentFinisher;
import org.hage.platform.component.provider.IMutableComponentInstanceProvider;
import org.hage.platform.component.services.core.CoreComponent;
import org.hage.platform.component.services.core.CoreComponentEvent;
import org.hage.platform.component.workplace.StopConditionFulfilledEvent;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.picocontainer.PicoContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static org.hage.platform.config.event.ConfigurationLoadRequestEvent.configurationLoadRequest;


@Slf4j
public class DefaultLifecycleEngine extends BaseLifecycleEngine implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Autowired
    private IMutableComponentInstanceProvider instanceProvider;

    @Autowired
    private CoreComponent coreComponent;

    @Autowired
    private EventBus eventBus;


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

    @Override
    protected void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder) {

        lifecycleBuilder

            .startWith(LifecycleState.OFFLINE)
            .terminateIn(LifecycleState.TERMINATED)

            .in(LifecycleState.OFFLINE)
            .on(LifecycleEvent.INITIALIZE).execute(new InitializationAction()).goTo(LifecycleState.INITIALIZED).commit()
            .in(LifecycleState.INITIALIZED)
            .on(LifecycleEvent.CONFIGURE).execute(new ConfigurationAction()).goTo(LifecycleState.CONFIGURED).commit()
            .in(LifecycleState.CONFIGURED)
            .on(LifecycleEvent.START_COMMAND).execute(new StartAction()).goTo(LifecycleState.RUNNING).commit()
            .in(LifecycleState.RUNNING)
            .on(LifecycleEvent.CORE_STARTING).goTo(LifecycleState.RUNNING).and()
            .on(LifecycleEvent.PAUSE).execute(new PauseAction()).goTo(LifecycleState.PAUSED).and()
            .on(LifecycleEvent.STOP_COMMAND).execute(new StopAction()).goTo(LifecycleState.STOPPED).commit()
            .in(LifecycleState.PAUSED)
            .on(LifecycleEvent.RESUME).execute(new ResumeAction()).goTo(LifecycleState.RUNNING).commit()
            .in(LifecycleState.STOPPED)
            .on(LifecycleEvent.CORE_STOPPED).execute(new CoreStoppedAction()).goTo(LifecycleState.STOPPED).and()
            .on(LifecycleEvent.CLEAR).execute(new ClearAction()).goTo(LifecycleState.INITIALIZED).commit()

            .inAnyState()
            .on(LifecycleEvent.EXIT).execute(new ExitAction()).goTo(LifecycleState.TERMINATED).and()
            .on(LifecycleEvent.ERROR).execute(new ErrorAction()).goTo(LifecycleState.FAILED).commit()

            .ifFailed()
            .fire(LifecycleEvent.ERROR)


            .shutdownWhenTerminated();
        //@formatter:on

        // Register shutdown hook, so we will be able to do a clean shutdown
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
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


    private class InitializationAction implements Action {

        @Override
        public void execute() {
            log.debug("Initializing LifecycleEngine.");

            notifyConfigurationCanBeLoaded();

            log.debug("Node has finished initialization.");
        }

        private void notifyConfigurationCanBeLoaded() {
            eventBus.post(configurationLoadRequest());
        }

    }


    private class ConfigurationAction implements Action {

        @SuppressWarnings("unchecked")
        @Override
        public void execute() {
            log.info("Configuring the computation.");

            coreComponent.configure();

            log.info("Node is configured.");
        }


    }


    private class StartAction implements Action {

        @Override
        public void execute() {
            log.info("Computation is starting.");

            try {
                coreComponent.start();
            } catch (final ComponentException e) {
                throw new LifecycleException("The core component could not start.", e);
            }
        }
    }


    private class PauseAction implements Action {

        @Override
        public void execute() {
            log.info("Computation is pausing.");

            coreComponent.pause();
        }
    }


    private class ResumeAction implements Action {

        @Override
        public void execute() {
            log.info("Computation is resuming.");

            coreComponent.resume();
        }
    }


    private class StopAction implements Action {

        @Override
        public void execute() {
            log.info("Computation is stopping.");

            coreComponent.stop();
        }
    }


    private class CoreStoppedAction implements Action {

        @Override
        public void execute() {
            log.debug("CoreComponent has stopped.");
        }
    }


    private class ClearAction implements Action {

        @Override
        public void execute() {
            log.info("Computation configuration is being removed.");

            coreComponent.resume();
        }
    }


    private class ExitAction implements Action {

        @Override
        public void execute() {
            log.debug("Node is terminating.");
            final long start = System.nanoTime();

            // Tears down in the whole hierarchy (similar to AGE-163). Can be removed when some @PreDestroy are
            // introduced or component stopping is supported at container level.
            if (instanceProvider instanceof PicoContainer) {
                ((PicoContainer) instanceProvider).accept(new StatefulComponentFinisher());
            } else {
                // fallback for other potential implementations
                final Collection<IStatefulComponent> statefulComponents =
                    instanceProvider.getInstances(IStatefulComponent.class);
                if (statefulComponents != null) {
                    for (final IStatefulComponent statefulComponent : statefulComponents) {
                        try {
                            statefulComponent.finish();
                        } catch (final ComponentException e) {
                            log.error("Exception during the teardown.", e);
                        }
                    }
                }
            }
            log.info("Node terminated.");

            if (log.isDebugEnabled()) {
                final Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();

                for (final Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
                    final Thread thread = entry.getKey();
                    if (!thread.equals(Thread.currentThread()) && thread.isAlive() && !thread.isDaemon()) {
                        log.debug("{} has not been shutdown properly.", entry.getKey());
                        for (final StackTraceElement e : entry.getValue()) {
                            log.debug("\t{}", e);
                        }
                    }
                }
            }

            final long elapsedTime = System.nanoTime() - start;
            log.debug("Shutdown took {} ms.", elapsedTime / 1000000);
        }
    }


    private class ErrorAction implements Action {

        @Override
        public void execute() {
            log.error("Node failed.");
            log.info("If you are running the node from the console, press Ctrl-C to exit.");
        }

    }


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
