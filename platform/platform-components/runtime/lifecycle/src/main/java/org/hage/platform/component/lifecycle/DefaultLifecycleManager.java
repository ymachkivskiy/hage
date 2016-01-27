package org.hage.platform.component.lifecycle;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.pico.visitor.StatefulComponentFinisher;
import org.hage.platform.component.pico.visitor.StatefulComponentInitializer;
import org.hage.platform.component.provider.IMutableComponentInstanceProvider;
import org.hage.platform.component.services.core.CoreComponent;
import org.hage.platform.component.services.core.CoreComponentEvent;
import org.hage.platform.component.services.core.LifecycleManager;
import org.hage.platform.component.workplace.StopConditionFulfilledEvent;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.hage.platform.util.fsm.CallableWithParameters;
import org.hage.platform.util.fsm.StateMachineService;
import org.hage.platform.util.fsm.StateMachineServiceBuilder;
import org.picocontainer.PicoContainer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Objects.toStringHelper;
import static org.hage.platform.config.event.ConfigurationLoadRequestEvent.configurationLoadRequest;


@Slf4j
public class DefaultLifecycleManager implements LifecycleManager, EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    private StateMachineService<State, Event> service;

    @Autowired
    private IMutableComponentInstanceProvider instanceProvider;

    @Autowired
    private CoreComponent coreComponent;

    @Autowired
    private EventBus eventBus;


    @PostConstruct
    private void initializeLifecycle() {

        //@formatter:off
        service = StateMachineServiceBuilder.<State, Event>create()
                .states(State.class).events(Event.class)
                .startWith(State.OFFLINE)
                .terminateIn(State.TERMINATED)

                .in(State.OFFLINE)
                .on(Event.INITIALIZE).execute(new InitializationAction()).goTo(State.INITIALIZED).commit()
                .in(State.INITIALIZED)
                .on(Event.CONFIGURE).execute(new ConfigurationAction()).goTo(State.CONFIGURED).commit()
                .in(State.CONFIGURED)
                .on(Event.START_COMMAND).execute(new StartAction()).goTo(State.RUNNING).commit()
                .in(State.RUNNING)
                .on(Event.CORE_STARTING).goTo(State.RUNNING).and()
                .on(Event.PAUSE).execute(new PauseAction()).goTo(State.PAUSED).and()
                .on(Event.STOP_COMMAND).execute(new StopAction()).goTo(State.STOPPED).commit()
                .in(State.PAUSED)
                .on(Event.RESUME).execute(new ResumeAction()).goTo(State.RUNNING).commit()
                .in(State.STOPPED)
                .on(Event.CORE_STOPPED).execute(new CoreStoppedAction()).goTo(State.STOPPED).and()
                .on(Event.CLEAR).execute(new ClearAction()).goTo(State.INITIALIZED).commit()

                .inAnyState()
                .on(Event.EXIT).execute(new ExitAction()).goTo(State.TERMINATED).and()
                .on(Event.ERROR).execute(new ErrorAction()).goTo(State.FAILED).commit()

                .ifFailed()
                .fire(Event.ERROR)
                .notificationCreator(new LifecycleStateNotificationCreator())
                .withEventBus(eventBus)
                .shutdownWhenTerminated()
                .build();
        //@formatter:on

        // Register shutdown hook, so we will be able to do a clean shutdown
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    public void performCommand(LifecycleMessage.LifecycleCommand command) {
        switch (command) {
            case FAIL:
                break;
            case NOTIFY:
                break;
            case PAUSE:
                service.fire(Event.PAUSE);
                break;
            case START:
                service.fire(Event.START_COMMAND);
                break;
            case STOP:
                service.fire(Event.STOP_COMMAND);
                break;
            case EXIT:
                service.fire(Event.EXIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        service.fire(Event.INITIALIZE);
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(service).toString();
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
            service.fire(Event.CONFIGURE);
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onCoreComponentEvent(CoreComponentEvent event) {
            log.debug("Core component Event: {}.", event);
            switch (event.getType()) {
                case CONFIGURED:
                    service.fire(Event.START_COMMAND);
                    break;
                case STARTING:
                    service.fire(Event.CORE_STARTING);
                    break;
                case STOPPED:
                    service.fire(Event.CORE_STOPPED);
                    break;
            }

        }

        @Subscribe
        public void onStopConditionFulfilledEvent(StopConditionFulfilledEvent event) {
            log.debug("Stop condition fulfilled event: {}.", event);
            service.fire(Event.STOP_COMMAND);
        }

        @Subscribe
        public void onExitRequestedEvent(ExitRequestedEvent event) {
            log.debug("Exit requested by event: {}.", event);
            service.fire(Event.EXIT);
        }

    }

    // Implementations of actions


    private class InitializationAction implements Runnable {

        @Override
        public void run() {
            log.debug("Initializing LifecycleManager.");

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

            initializeStatefullComponents();

            log.info("Node is configured.");
        }

        private void initializeStatefullComponents() {
            log.debug("Initialising required components.");
            // initialize in the whole hierarchy (see AGE-163). Can be removed when some @PostConstruct are introduced
            // or component starting is supported at container level.
            if (instanceProvider instanceof PicoContainer) {
                ((PicoContainer) instanceProvider).accept(new StatefulComponentInitializer());
            } else {
                //fallback for other potential implementations
                instanceProvider.getInstances(IStatefulComponent.class);
            }
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


    private class ErrorAction implements CallableWithParameters<Throwable> {

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
            if (!service.terminated() && !service.isTerminating()) {
                service.fire(Event.EXIT);
                try {
                    Thread.sleep(2000); // Simple wait to let other threads terminate properly.
                } catch (final InterruptedException ignored) {
                    // Ignore
                }
            }
        }
    }

}
