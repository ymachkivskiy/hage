package org.jage.configuration.service;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.ConfigurationUpdatedEvent;
import org.jage.bus.EventBus;
import org.jage.configuration.communication.ConfigurationLoadedEvent;
import org.jage.configuration.communication.ConfigurationMessage;
import org.jage.configuration.communication.ConfigurationMessage.MessageType;
import org.jage.configuration.communication.ConfigurationRemoteServiceChanelEndpoint;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.IComponentDefinition;
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


@ThreadSafe
@Slf4j
public class ConfigurationHubService extends AbstractScheduledService
        implements IStatefulComponent {

    private final AtomicReference<Collection<IComponentDefinition>> configuration = new AtomicReference<>(null);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ConfigurationRemoteServiceChanelEndpoint remoteChanel;

    @Override
    public void init() {

        remoteChanel.registerConsumerHandler(message -> message.getType() == MessageType.REQUEST && configuration.get() != null,
                                             message -> distribute()
        );
        remoteChanel.registerConsumerHandler(message -> message.getType() == MessageType.DISTRIBUTE,
                                             message -> {
                                                 final Serializable payload = message.getPayload();
                                                 if(!(payload instanceof Collection)) {
                                                     throw new NullPointerException(String.format("Configuration payload was null. Faulty message was sent by %s.", message));
                                                 }

                                                 if(configuration.compareAndSet(null, (Collection<IComponentDefinition>) payload)) {
                                                     notifyConfigurationUpdated();
                                                 }
                                             }
        );

        eventBus.register(this);
        startAsync();
    }

    @Override
    public boolean finish() {
        stopAsync();
        awaitTerminated();
        return true;
    }

    private void distribute() {
        assert configuration.get() != null;
        // Ensure serializable collection.
        final ArrayList<IComponentDefinition> definitions = newArrayList(configuration.get());

        final ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.DISTRIBUTE, definitions);

        remoteChanel.sendMessage(message);
    }

    private void notifyConfigurationUpdated() {
        log.debug("Computation configuration set.");
        eventBus.post(new ConfigurationUpdatedEvent(unmodifiableList(newArrayList(configuration.get()))));
    }

    @Subscribe
    public void onConfigurationLoaded(@Nonnull final ConfigurationLoadedEvent event) {
        log.debug("Configuration loaded event: {}.", event);
        configuration.set(event.getLoadedComponents());
        notifyConfigurationUpdated();
    }

    @Override
    protected void runOneIteration() {
        if(configuration.get() == null) {
            log.debug("No configuration. Broadcasting the request.");
            final ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.REQUEST);
            remoteChanel.sendMessage(message);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return newFixedRateSchedule(1, 1, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("configuration", configuration.get()).toString();
    }
}
