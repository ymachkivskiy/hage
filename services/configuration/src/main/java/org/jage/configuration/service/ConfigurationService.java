package org.jage.configuration.service;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.ConfigurationUpdatedEvent;
import org.jage.bus.EventBus;
import org.jage.configuration.communication.ConfigurationLoadedEvent;
import org.jage.configuration.communication.ConfigurationServiceRemoteChanel;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.IComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import static java.util.Collections.unmodifiableList;


@ThreadSafe
@Slf4j
public class ConfigurationService extends AbstractScheduledService
        implements IStatefulComponent {

    private final AtomicReference<Collection<IComponentDefinition>> configuration = new AtomicReference<>(null);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ConfigurationServiceRemoteChanel remoteConfigurationChanel;

    @Override
    public void init() {
        eventBus.register(this);
        startAsync();
    }

    @Override
    public boolean finish() {
        stopAsync();
        awaitTerminated();
        return true;
    }

    public void distributeConfiguration() {
        if(configuration.get() != null) {
            remoteConfigurationChanel.distributeConfiguration(configuration.get());
        }
    }

    public void updateConfiguration(Collection<IComponentDefinition> configuration) {
        if(this.configuration.compareAndSet(null, configuration)) {
            notifyConfigurationUpdated();
        }
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
            remoteConfigurationChanel.acquireConfiguration();
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
