package org.hage.platform.simconf;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.event.CoreConfiguredEvent;
import org.hage.platform.node.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

@SingletonComponent
@Slf4j
class SimulationConfiguratorAggregate implements SimulationConfigurator {

    @Autowired(required = false)
    private List<CoreConfigurer> coreConfigurers = emptyList();

    @Autowired
    private EventBus eventBus;

    @Override
    public void configureSimulation(Configuration configuration) {
        log.debug("Configuring core using {} configurers with configuration {}", coreConfigurers.size(), configuration);

        for (CoreConfigurer coreConfigurer : coreConfigurers) {
            coreConfigurer.configureWith(configuration);
        }

        eventBus.post(new CoreConfiguredEvent());
    }

}
