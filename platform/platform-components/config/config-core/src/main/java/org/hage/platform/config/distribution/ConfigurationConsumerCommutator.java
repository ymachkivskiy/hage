package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationConsumer;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.execution.event.CoreReadyEvent;
import org.hage.platform.component.rate.RateConfigurationConsumer;
import org.hage.platform.component.runtime.ExecutionUnitRepositoryConfigurator;
import org.hage.platform.component.structure.connections.StructureConfigurator;
import org.hage.platform.config.Configuration;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
class ConfigurationConsumerCommutator implements ConfigurationConsumer {

    @Autowired
    private StructureConfigurator structureConfigurator;
    @Autowired
    private ExecutionUnitRepositoryConfigurator executionUnitRepositoryConfigurator;
    @Autowired
    private RateConfigurationConsumer rateConfigurationConsumer;
    @Autowired
    private MutableInstanceContainer mutableInstanceContainer;
    @Autowired
    private EventBus eventBus;

    @Override
    public void acceptConfiguration(Configuration configuration) {
        log.debug("Accepting configuration '{}'", configuration);

        registerGlobalComponents(configuration);

        rateConfigurationConsumer.acceptRateConfiguration(configuration.getCommon().getRatingConfig());
        structureConfigurator.configure(configuration.getCommon().getStructureDefinition());
        executionUnitRepositoryConfigurator.populateWith(configuration.getSpecific().getPopulation());

        eventBus.post(new CoreReadyEvent());
    }

    private void registerGlobalComponents(Configuration configuration) {
        Collection<IComponentDefinition> globalComponents = configuration.getCommon().getGlobalComponents();

        for (IComponentDefinition component : globalComponents) {
            mutableInstanceContainer.addComponent(component);
        }

        mutableInstanceContainer.initializeStatefulComponents();
    }
}
