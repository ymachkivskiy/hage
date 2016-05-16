package org.hage.platform.component.simulationconfig;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.CoreReadyEvent;
import org.hage.platform.component.runtime.init.ContainerConfigurator;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.init.StopConditionConfigurator;
import org.hage.platform.component.runtime.init.UnitPropertiesConfigurator;
import org.hage.platform.component.structure.connections.StructureConfigurator;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
@Slf4j
class MainPlatformSimulationConfigurator implements ConfigurationConsumer {

    @Autowired
    private StructureConfigurator structureConfigurator;
    @Autowired
    private PopulationInitializer populationInitializer;
    @Autowired
    private ContainerConfigurator containerConfigurator;
    @Autowired
    private StopConditionConfigurator stopConditionConfigurator;
    @Autowired
    private UnitPropertiesConfigurator unitPropertiesConfigurator;

    @Autowired
    private EventBus eventBus;

    @Override
    public void acceptConfiguration(Configuration configuration) {
        log.debug("Accepting configuration '{}'", configuration);

        containerConfigurator.setupConfiguration(configuration.getCommon().getContainerConfiguration());

        structureConfigurator.configure(configuration.getCommon().getStructureDefinition());
        populationInitializer.initializeWith(configuration.getSpecific().getPopulation());
        stopConditionConfigurator.configureWith(configuration.getCommon().getStopCondition());
        unitPropertiesConfigurator.configureWith(configuration.getCommon().getUnitPropertiesConfiguratorClazz());

        eventBus.post(new CoreReadyEvent());
    }

}
