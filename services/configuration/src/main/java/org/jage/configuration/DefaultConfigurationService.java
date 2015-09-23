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

package org.jage.configuration;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.ConfigurationUpdatedEvent;
import org.jage.bus.EventBus;
import org.jage.communication.CommunicationChannel;
import org.jage.communication.CommunicationManager;
import org.jage.communication.MessageSubscriber;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.services.core.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import static java.util.Collections.unmodifiableList;


/**
 * A default configuration service that works in two possible modes:
 * <ul>
 * <li>waiting for the configuration from other nodes;
 * <li>sending configuration to other nodes.
 * </ul>
 *
 * @author AGH AgE Team
 */
@ThreadSafe
@Slf4j
public class DefaultConfigurationService extends AbstractScheduledService
        implements ConfigurationService, IStatefulComponent,
                   MessageSubscriber<ConfigurationMessage> {

    private final AtomicReference<Collection<IComponentDefinition>> configuration = new AtomicReference<>(null);

    @Autowired
    private CommunicationManager communicationManager;
    @Autowired
    private EventBus eventBus;

    private CommunicationChannel<ConfigurationMessage> communicationChannel;


    @Override
    public void init() {
        communicationChannel = communicationManager.getCommunicationChannelForService(SERVICE_NAME);
        communicationChannel.subscribe(this);
        eventBus.register(this);
        startAsync();
    }

    @Override
    public boolean finish() {
        stopAsync();
        communicationChannel.unsubscribe(this);
        awaitTerminated();
        return true;
    }

    @Subscribe
    public void onConfigurationLoaded(@Nonnull final ConfigurationLoadedEvent event) {
        log.debug("Configuration loaded event: {}.", event);
        configuration.set(event.getLoadedComponents());
        notifyConfigurationUpdated();
    }

    private void notifyConfigurationUpdated() {
        log.debug("Computation configuration set.");
        eventBus.post(new ConfigurationUpdatedEvent(unmodifiableList(newArrayList(configuration.get()))));
    }

    @Override
    protected void runOneIteration() {
        if(configuration.get() == null) {
            log.debug("No configuration. Broadcasting the request.");
            final ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.REQUEST);
            communicationChannel.publish(message);
        }
    }

    @Override
    protected void startUp() { /* Empty */
    }

    @Override
    protected void shutDown() { /* Empty */
    }

    @Override
    protected Scheduler scheduler() {
        return newFixedRateSchedule(1, 1, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("configuration", configuration.get()).toString();
    }

    @Override
    public void onMessage(@Nonnull final ConfigurationMessage message) {
        log.debug("Received message: {}.", message);

        final ConfigurationMessage.MessageType type = message.getType();
        switch(type) {
            case REQUEST: // Sb requests a configuration
                if(configuration.get() != null) {
                    distribute();
                }
                break;
            case DISTRIBUTE: // Sb sends a configuration
                final Serializable payload = message.getPayload();
                if(!(payload instanceof Collection)) {
                    throw new NullPointerException(String.format("Configuration payload was null. Faulty message was sent by %s.", message));
                }

                if(configuration.compareAndSet(null, (Collection<IComponentDefinition>) payload)) {
                    notifyConfigurationUpdated();
                }
                break;
        }
    }

    /**
     * Distributes provided component definitions among other nodes
     * participating in the computation.
     * <p>
     * <p>
     * Note: this implementation does not care whether anyone received the
     * configuration.
     */
    protected void distribute() {
        assert configuration.get() != null;
        // Ensure serializable collection.
        final ArrayList<IComponentDefinition> definitions = newArrayList(configuration.get());

        final ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.DISTRIBUTE, definitions);

        communicationChannel.publish(message);
    }
}
