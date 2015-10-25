/**
 * Copyright (C) 2006 - 2012
 * Pawel Kedzior
 * Tomasz Kmiecik
 * Kamil Pietak
 * Krzysztof Sikora
 * Adam Wos
 * Lukasz Faber
 * Daniel Krzywicki
 * and other students of AGH University of Science and Technology.
 * <p>
 * This file is part of AgE.
 * <p>
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2013-08-11
 * $Id$
 */

package org.jage.configuration.service;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.EventBus;
import org.jage.configuration.data.ComputationConfiguration;
import org.jage.configuration.event.ConfigurationLoadRequestEvent;
import org.jage.configuration.event.ConfigurationLoadedEvent;
import org.jage.platform.argument.RuntimeArgumentsService;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

import static com.google.common.base.Objects.toStringHelper;


/**
 * Provides a way to load computation configuration from a file.</p>
 * <p>
 * This service reacts on the "INITIALIZED" event of the lifecycle manager and loads a computation configuration
 * from file. The configuration is sent via the event bus in {@link ConfigurationLoadedEvent}.
 */
@ThreadSafe
@Slf4j
public class ConfigurationLoaderService implements IStatefulComponent {

    private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

    @Autowired
    private RuntimeArgumentsService argumentsService;
    @Autowired
    private IConfigurationLoader configurationLoader;
    @Autowired
    private EventBus eventBus;

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        return true;
    }

    @Subscribe
    public void onLifecycleEvent(@Nonnull final ConfigurationLoadRequestEvent event) {
        tryLoadAndPropagateConfiguration();
    }

    private void tryLoadAndPropagateConfiguration() {
        final String configFilePath = argumentsService.getCustomOption(COMPUTATION_CONFIGURATION);
        if (configFilePath != null) {
            loadAndPropagateConfiguration(configFilePath);
        } else {
            log.info("No computation configuration.");
        }
    }

    private void loadAndPropagateConfiguration(String configFilePath) {
        Collection<IComponentDefinition> computationComponents = getComputationComponentsDefinitions(configFilePath);
        propagateComputationComponentsDefinitions(computationComponents);
    }

    private Collection<IComponentDefinition> getComputationComponentsDefinitions(String configFilePath) {
        log.info("Loading computation configuration from {}.", configFilePath);

        final Collection<IComponentDefinition> computationComponents;
        try {
            computationComponents = configurationLoader.loadConfiguration(configFilePath);
        } catch (final ConfigurationException e) {
            log.error("Cannot load configuration from {}.", configFilePath, e);
            throw new ComponentException(e);
        }
        return computationComponents;
    }

    private void propagateComputationComponentsDefinitions(Collection<IComponentDefinition> computationComponents) {
        eventBus.post(new ConfigurationLoadedEvent(new ComputationConfiguration(computationComponents)));
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
