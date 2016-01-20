package org.hage.configuration.service;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.configuration.event.ConfigurationUpdatedEvent;
import org.hage.platform.config.def.ComputationConfiguration;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@ToString
@ThreadSafe
@Slf4j
public class ConfigurationStorageService {

    private Optional<ComputationConfiguration> configuration = empty();

    @Autowired
    private EventBus eventBus;

    public boolean hasConfiguration() {
        return configuration.isPresent();
    }

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
}