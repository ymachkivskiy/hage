package org.hage.platform.config;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventBus;
import org.hage.util.concurrency.BlockingElementHolder;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
class ConfigurationHub implements ConfigurationProvider, ConfigurationStorage {

    private final BlockingElementHolder<ComputationConfiguration> confgurationHolder = new BlockingElementHolder<>();

    @Autowired
    private EventBus eventBus;

    @Override
    public boolean hasConfiguration() {
        return confgurationHolder.hasElement();
    }

    @Override
    public void updateConfiguration(ComputationConfiguration configuration) {
        if (confgurationHolder.offerIfAbsent(configuration) == false) {
            log.warn("Trying to set configuration when there is all ready one");
        } else {
            eventBus.post(new ConfigurationUpdatedEvent(configuration));
        }
    }

    @Override
    public ComputationConfiguration provideConfiguration() {
        log.info("Requesting configuration");

        ComputationConfiguration configuration = confgurationHolder.acquire();

        log.info("Returning configuration: {}", configuration);

        return configuration;
    }

}
