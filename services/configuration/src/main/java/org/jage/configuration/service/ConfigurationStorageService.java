package org.jage.configuration.service;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.EventBus;
import org.jage.configuration.communication.ConfigurationServiceRemoteChanel;
import org.jage.configuration.event.ConfigurationLoadedEvent;
import org.jage.configuration.event.ConfigurationUpdatedEvent;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.config.ComputationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;


@ThreadSafe
@Slf4j
public class ConfigurationStorageService extends AbstractScheduledService
        implements IStatefulComponent {

    private final AtomicReference<ComputationConfiguration> configuration = new AtomicReference<>(null);

    @Autowired
    private EventBus eventBus;
    @Autowired
    private ConfigurationServiceRemoteChanel remoteConfigurationChanel;

    @Override
    public void init() {
        startAsync();
    }

    @Override
    public boolean finish() {
        stopAsync();
        awaitTerminated();
        return true;
    }

    public void distributeConfiguration() {
        if (configuration.get() != null) {
            remoteConfigurationChanel.distributeConfiguration(configuration.get());
        }
    }

    public void updateConfiguration(ComputationConfiguration configuration) {
        if (this.configuration.compareAndSet(null, configuration)) {
            notifyConfigurationUpdated();
        }
    }

    private void notifyConfigurationUpdated() {
        log.debug("Computation configuration set.");
        eventBus.post(new ConfigurationUpdatedEvent(configuration.get()));
    }

    @Override
    protected void runOneIteration() {
        if (configuration.get() == null) {
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
