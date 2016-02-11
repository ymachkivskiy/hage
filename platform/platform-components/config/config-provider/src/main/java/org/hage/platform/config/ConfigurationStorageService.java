package org.hage.platform.config;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@Slf4j
class ConfigurationStorageService implements ConfigurationProvider, ConfigurationStorage {

    private Optional<ComputationConfiguration> configuration = empty();

    @Autowired
    private EventBus eventBus;

    @Override
    public boolean hasConfiguration() {
        return configuration.isPresent();
    }

    @Override
    public void updateConfiguration(ComputationConfiguration configuration) {
        if (this.configuration.isPresent()) {
            log.warn("Trying to set configuration when there is all ready one");
        } else {
            Optional<ComputationConfiguration> newConfiguration = ofNullable(configuration);
            if (newConfiguration.isPresent()) {
                this.configuration = newConfiguration;
                eventBus.post(new ConfigurationUpdatedEvent(configuration));
            }
        }
    }

    @Override
    public ComputationConfiguration provideConfiguration() {
        // TODO: 10.02.16 lock till has no config
        return configuration.get();
    }

}
