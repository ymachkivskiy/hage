package org.hage.platform.component.simulationconfig;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.rate.RateConfigurationConsumer;
import org.hage.platform.component.runtime.event.CoreReadyEvent;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.init.ContainerConfigurator;
import org.hage.platform.component.structure.connections.StructureConfigurator;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@SingletonComponent
@Slf4j
class MainPlatformSimulationConfigurator implements ConfigurationConsumer {

    @Autowired
    private StructureConfigurator structureConfigurator;
    @Autowired
    private PopulationInitializer populationInitializer;
    @Autowired
    private RateConfigurationConsumer rateConfigurationConsumer;

    @Autowired
    private ContainerConfigurator containerConfigurator;
    @Autowired
    private EventBus eventBus;

    @Override
    public void acceptConfiguration(Configuration configuration) {
        log.debug("Accepting configuration '{}'", configuration);

        containerConfigurator.setupConfiguration(configuration.getCommon().getContainerConfiguration());

        // TODO: rate config will no longer be part of simulation config
        rateConfigurationConsumer.acceptRateConfiguration(configuration.getCommon().getRatingConfig());
        structureConfigurator.configure(configuration.getCommon().getStructureDefinition());
        populationInitializer.initializeWith(configuration.getSpecific().getPopulation());

        eventBus.post(new CoreReadyEvent());
    }

}
