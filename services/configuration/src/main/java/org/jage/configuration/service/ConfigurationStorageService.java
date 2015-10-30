package org.jage.configuration.service;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.EventBus;
import org.jage.configuration.event.ConfigurationUpdatedEvent;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.config.ComputationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@ToString
@ThreadSafe
@Slf4j
public class ConfigurationStorageService implements IStatefulComponent {

    private Optional<ComputationConfiguration> configuration = empty();

    @Autowired
    private EventBus eventBus;

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        eventBus.unregister(this);
        return true;
    }

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
