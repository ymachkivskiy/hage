package org.hage.platform.component.lifecycle;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.lifecycle.event.LifecycleEvent;
import org.hage.platform.component.lifecycle.event.LifecycleState;
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

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Objects.toStringHelper;
import static org.hage.platform.config.event.ConfigurationLoadRequestEvent.configurationLoadRequest;


@Slf4j
public class DefaultLifecycleEngine implements LifecycleEngine, EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    private LifecycleEngineService engine;

    @Autowired
    private IMutableComponentInstanceProvider instanceProvider;

    @Autowired
    private CoreComponent coreComponent;

    @Autowired
    private EventBus eventBus;


    @PostConstruct
    private void initializeLifecycle() {

        //@formatter:off
        engine = LifecycleEngineServiceBuilder.engineBuilder()
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
                .withEventBus(eventBus)
                .shutdownWhenTerminated()
                .build();
        //@formatter:on

        // Register shutdown hook, so we will be able to do a clean shutdown
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    public void performCommand(LifecycleCommand command) {
        switch (command) {
            case FAIL:
                break;
            case NOTIFY:
                break;
            case PAUSE:
                engine.fire(LifecycleEvent.PAUSE);
                break;
            case START:
                engine.fire(LifecycleEvent.START_COMMAND);
                break;
            case STOP:
                engine.fire(LifecycleEvent.STOP_COMMAND);
                break;
            case EXIT:
                engine.fire(LifecycleEvent.EXIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        engine.fire(LifecycleEvent.INITIALIZE);
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(engine).toString();
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
            engine.fire(LifecycleEvent.CONFIGURE);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onCoreComponentEvent(CoreComponentEvent event) {
            log.debug("Core component Event: {}.", event);
            switch (event.getType()) {
                case CONFIGURED:
                    engine.fire(LifecycleEvent.START_COMMAND);
                    break;
                case STARTING:
                    engine.fire(LifecycleEvent.CORE_STARTING);
                    break;
                case STOPPED:
                    engine.fire(LifecycleEvent.CORE_STOPPED);
                    break;
            }

        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onStopConditionFulfilledEvent(StopConditionFulfilledEvent event) {
            log.debug("Stop condition fulfilled event: {}.", event);
            engine.fire(LifecycleEvent.STOP_COMMAND);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onExitRequestedEvent(ExitRequestedEvent event) {
            log.debug("Exit requested by event: {}.", event);
            engine.fire(LifecycleEvent.EXIT);
        }

    }

    // Implementations of actions


    private class InitializationAction implements Runnable {

        @Override
        public void run() {
            log.debug("Initializing LifecycleEngine.");

            notifyConfigurationCanBeLoaded();

            log.debug("Node has finished initialization.");
        }

        private void notifyConfigurationCanBeLoaded() {
            eventBus.post(configurationLoadRequest());
        }

    }


    private class ConfigurationAction implements Runnable {

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            log.info("Configuring the computation.");

            coreComponent.configure();

            log.info("Node is configured.");
        }



    }


    private class StartAction implements Runnable {

        @Override
        public void run() {
            log.info("Computation is starting.");

            try {
                coreComponent.start();
            } catch (final ComponentException e) {
                throw new LifecycleException("The core component could not start.", e);
            }
        }
    }


    private class PauseAction implements Runnable {

        @Override
        public void run() {
            log.info("Computation is pausing.");

            coreComponent.pause();
        }
    }


    private class ResumeAction implements Runnable {

        @Override
        public void run() {
            log.info("Computation is resuming.");

            coreComponent.resume();
        }
    }


    private class StopAction implements Runnable {

        @Override
        public void run() {
            log.info("Computation is stopping.");

            coreComponent.stop();
        }
    }


    private class CoreStoppedAction implements Runnable {

        @Override
        public void run() {
            log.debug("CoreComponent has stopped.");
        }
    }


    private class ClearAction implements Runnable {

        @Override
        public void run() {
            log.info("Computation configuration is being removed.");

            coreComponent.resume();
        }
    }


    private class ExitAction implements Runnable {

        @Override
        public void run() {
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


    private class ErrorAction implements CallableWithParameter<Throwable> {

        @Override
        public void call(final Throwable parameter) {
            log.error("Node failed with exception.", parameter);

            log.info("If you are running the node from the console, press Ctrl-C to exit.");
        }
    }


    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            log.debug("Shutdown hook called.");
            if (!engine.terminated() && !engine.isTerminating()) {
                engine.fire(LifecycleEvent.EXIT);
                try {
                    Thread.sleep(2000); // Simple wait to let other threads terminate properly.
                } catch (final InterruptedException ignored) {
                    // Ignore
                }
            }
        }
    }

}
