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
 * Created: 2013-08-11
 * $Id$
 */

package org.jage.configuration;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.bus.EventBus;
import org.jage.lifecycle.LifecycleStateChangedEvent;
import org.jage.platform.argument.RuntimeArgumentsService;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.services.core.LifecycleManager;

import com.google.common.eventbus.Subscribe;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Provides a way to load computation configuration from a file.</p>
 *
 * This service reacts on the "INITIALIZED" event of the lifecycle manager and loads a computation configuration
 * from file. The configuration is sent via the event bus in {@link ConfigurationLoadedEvent}.
 */
@ThreadSafe
public class ConfigurationLoaderService implements IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationLoaderService.class);

	private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

	@Inject private RuntimeArgumentsService argumentsService;

	@Inject private IConfigurationLoader configurationLoader;

	@Inject private EventBus eventBus;

	@Override public void init() {
		eventBus.register(this);
	}

	@Override public boolean finish() {
		return true;
	}

	@Subscribe public void onLifecycleEvent(@Nonnull final LifecycleStateChangedEvent event) {
		final LifecycleManager.State newState = event.getNewState();

		if (LifecycleManager.State.INITIALIZED.equals(newState)) {
			final String configFilePath = argumentsService.getCustomOption(COMPUTATION_CONFIGURATION);

			if (configFilePath != null) {
				log.info("Loading computation configuration from {}.", configFilePath);
				final Collection<IComponentDefinition> computationComponents;
				try {
					computationComponents = configurationLoader.loadConfiguration(configFilePath);
				} catch (final ConfigurationException e) {
					log.error("Cannot load configuration from {}.", configFilePath, e);
					throw new ComponentException(e);
				}

				eventBus.post(new ConfigurationLoadedEvent(computationComponents));
			} else {
				log.info("No computation configuration.");
			}
		}
	}

	@Override
	public String toString() {
		return toStringHelper(this).toString();
	}
}
