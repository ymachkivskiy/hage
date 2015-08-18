/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-08-21
 * $Id$
 */

package org.jage.lifecycle;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jage.bus.ConfigurationUpdatedEvent;
import org.jage.bus.EventBus;
import org.jage.communication.CommunicationChannel;
import org.jage.communication.CommunicationManager;
import org.jage.communication.DefaultCommunicationManager;
import org.jage.communication.MessageSubscriber;
import org.jage.lifecycle.LifecycleMessage.LifecycleCommand;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.argument.RuntimeArgumentsService;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.pico.visitor.StatefulComponentFinisher;
import org.jage.platform.component.pico.visitor.StatefulComponentInitializer;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.ConfigurationLoader;
import org.jage.platform.fsm.CallableWithParameters;
import org.jage.platform.fsm.StateMachineService;
import org.jage.platform.fsm.StateMachineServiceBuilder;
import org.jage.services.core.CoreComponent;
import org.jage.services.core.CoreComponentEvent;
import org.jage.services.core.LifecycleManager;
import org.jage.workplace.StopConditionFulfilledEvent;

import com.google.common.eventbus.Subscribe;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A default lifecycle manager for generic AgE nodes.
 *
 * @author AGH AgE Team
 */
public class DefaultLifecycleManager
		implements IMutableComponentInstanceProviderAware, LifecycleManager, IStatefulComponent,
		MessageSubscriber<LifecycleMessage> {

	/**
	 * The name of the node configuration file parameter.
	 */
	public static final String NODE_CONFIGURATION_FILE_OPTION = "age.node.conf";

	private static final Logger log = LoggerFactory.getLogger(DefaultLifecycleManager.class);

	private final StateMachineService<State, Event> service;

	private IMutableComponentInstanceProvider instanceProvider;

	private CoreComponent coreComponent;

	@Inject
	private RuntimeArgumentsService argumentsService;

	private CommunicationChannel<LifecycleMessage> communicationChannel;

	/**
	 * Constructs a new lifecycle manager.
	 */
	public DefaultLifecycleManager() {
		final StateMachineServiceBuilder<State, Event> builder = StateMachineServiceBuilder.create();

		//@formatter:off
		builder
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

				.notifyWithType(LifecycleStateChangedEvent.class)
				.shutdownWhenTerminated();

		service = builder.build();
		//@formatter:on

		// Register shutdown hook, so we will be able to do a clean shutdown
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

	// Interface methods that translate environment changes to events

	@Override
	public void setMutableComponentInstanceProvider(final IMutableComponentInstanceProvider provider) {
		instanceProvider = provider;
	}

	@Override
    public void onMessage(final LifecycleMessage message) {
		final LifecycleCommand command = message.getCommand();
		switch (command) {
			case FAIL:
				break;
			case NOTIFY:
				handleNotifyCommand((Map<String, Object>)message.getPayload());
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

	private void handleNotifyCommand(final Map<String, Object> payload) {
		final Map<String, Object> data = checkNotNull(payload);

		for (final Entry<String, Object> entry : data.entrySet()) {
			// XXX
		}
	}

	@Override
	public void init() {
		service.fire(Event.INITIALIZE);
	}

	@Override
	public boolean finish() {
		return true;
	}

	@Subscribe
    public void onConfigurationUpdated(@Nonnull final ConfigurationUpdatedEvent event) {
		log.debug("Configuration updated event: {}.", event);
		service.fire(Event.CONFIGURE);
	}

	@Subscribe
    public void onCoreComponentEvent(@Nonnull final CoreComponentEvent event) {
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
    public void onStopConditionFulfilledEvent(@Nonnull final StopConditionFulfilledEvent event) {
		log.debug("Stop condition fulfilled event: {}.", event);
		service.fire(Event.STOP_COMMAND);
	}

	@Subscribe
    public void onExitRequestedEvent(@Nonnull final ExitRequestedEvent event) {
		log.debug("Exit requested by event: {}.", event);
		service.fire(Event.EXIT);
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(service).toString();
	}

	// Implementations of actions

	private class InitializationAction implements Runnable {
		@Override
		public void run() {
			log.debug("Initializing LifecycleManager.");

            registerConfigurationLoader();
            initializeNodeConfiguration();
            initializeStatefullComponents();
            initializeCommunicationChanel();
            initializeEventBus();
            initializeCoreComponent();

			log.debug("Node has finished initialization.");
		}

        private void registerConfigurationLoader() {
            instanceProvider.addComponent(ConfigurationLoader.class);
        }

        private void initializeNodeConfiguration() {
            final String configFilePath = argumentsService.getCustomOption(NODE_CONFIGURATION_FILE_OPTION);

            if (configFilePath == null) {
                throw new InvalidRuntimeArgumentsException(String.format(
                        "The node config file name parameter is missing. Specify the correct path " +
                                "to the configuration file using -D%s option", NODE_CONFIGURATION_FILE_OPTION));
            }

            try {
                log.debug("Loading configuration from {}.", configFilePath);
                final Collection<IComponentDefinition> nodeComponents =
                        instanceProvider.getInstance(IConfigurationLoader.class).loadConfiguration(configFilePath);

                for (final IComponentDefinition def : nodeComponents) {
                    instanceProvider.addComponent(def);
                }
                instanceProvider.verify();
            } catch (final ComponentException | ConfigurationException e) {
                throw new LifecycleException("Cannot perform components initialization.", e);
            }
        }

        private void initializeStatefullComponents() {
            log.debug("Initialising required components.");
            // initialize in the whole hierarchy (see AGE-163). Can be removed when some @PostConstruct are introduced
            // or component starting is supported at container level.
            if (instanceProvider instanceof PicoContainer) {
                ((PicoContainer)instanceProvider).accept(new StatefulComponentInitializer());
            } else {
                //fallback for other potential implementations
                instanceProvider.getInstances(IStatefulComponent.class);
            }
        }

        private void initializeCommunicationChanel() {
            final CommunicationManager communicationService = instanceProvider.getInstance(DefaultCommunicationManager.class);

            if (communicationService == null) {
                throw new LifecycleException("There is no CommunicationService in the platform.");
            }
            log.debug("Communication service: {}.", communicationService);

            communicationChannel = communicationService.getCommunicationChannelForService(SERVICE_NAME);
            communicationChannel.subscribe(DefaultLifecycleManager.this);

            log.debug("Communication channel: {}.", communicationChannel);
        }

        private void initializeEventBus() {
            final EventBus eventBus = instanceProvider.getInstance(EventBus.class);
            service.setEventBus(eventBus);
            eventBus.register(DefaultLifecycleManager.this);
            log.debug("Event bus: {}.", eventBus);
        }

        private void initializeCoreComponent() {
            coreComponent = instanceProvider.getInstance(CoreComponent.class);
            if (coreComponent == null) {
                throw new LifecycleException("Core component (CoreComponent) is missing. Cannot run the computation.");
            }
            log.debug("Core component: {}.", coreComponent);
        }
    }

	private class ConfigurationAction implements Runnable {
		@SuppressWarnings("unchecked") @Override
		public void run() {
			log.debug("Configuring the computation.");
			log.debug("Node is configured.");
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
				((PicoContainer)instanceProvider).accept(new StatefulComponentFinisher());
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
