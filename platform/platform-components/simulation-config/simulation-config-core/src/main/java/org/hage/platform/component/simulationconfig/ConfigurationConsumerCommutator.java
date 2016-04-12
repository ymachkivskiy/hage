package org.hage.platform.component.simulationconfig;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.runtime.event.CoreReadyEvent;
import org.hage.platform.component.rate.RateConfigurationConsumer;
import org.hage.platform.component.runtime.init.RuntimeInitializer;
import org.hage.platform.component.structure.connections.StructureConfigurator;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@SingletonComponent
@Slf4j
class ConfigurationConsumerCommutator implements ConfigurationConsumer {

    @Autowired
    private StructureConfigurator structureConfigurator;
    @Autowired
    private RuntimeInitializer runtimeInitializer;
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
        runtimeInitializer.initializeWith(configuration.getSpecific().getPopulation());

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
