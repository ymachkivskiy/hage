package org.hage.platform.component.simulationconfig;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.simulationconfig.event.ConfigurationUpdatedEvent;
import org.hage.platform.node.bus.EventBus;
import org.hage.util.concurrency.BlockingElementHolder;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class SimulationConfigurationHub implements ConfigurationProvider, ConfigurationStorage {

    private final BlockingElementHolder<Configuration> confgurationHolder = new BlockingElementHolder<>();

    @Autowired
    private EventBus eventBus;

    @Override
    public boolean hasConfiguration() {
        return confgurationHolder.hasElement();
    }

    @Override
    public void updateConfiguration(Configuration configuration) {
        if (confgurationHolder.offerIfAbsent(configuration) == false) {
            log.warn("Trying to set configuration when there is all ready one");
        } else {
            eventBus.post(new ConfigurationUpdatedEvent(configuration));
        }
    }

    @Override
    public Configuration provideConfiguration() {
        log.info("Requesting configuration");

        Configuration configuration = confgurationHolder.acquire();

        log.info("Returning configuration: {}", configuration);

        return configuration;
    }

}
