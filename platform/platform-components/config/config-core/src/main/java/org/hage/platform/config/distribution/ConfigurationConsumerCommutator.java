package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationConsumer;
import org.hage.platform.component.rate.RateConfigurationConsumer;
import org.hage.platform.component.runtime.ExecutionUnitRepositoryConfigurator;
import org.hage.platform.component.structure.StructureRepositoryConfigurator;
import org.hage.platform.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ConfigurationConsumerCommutator implements ConfigurationConsumer {

    @Autowired
    private StructureRepositoryConfigurator structureRepositoryConfigurator;
    @Autowired
    private ExecutionUnitRepositoryConfigurator executionUnitRepositoryConfigurator;
    @Autowired
    private RateConfigurationConsumer rateConfigurationConsumer;

    @Override
    public void acceptConfiguration(Configuration configuration) {
        log.debug("Accepting configuration '{}'",configuration);

        rateConfigurationConsumer.acceptRateConfiguration(configuration.getCommon().getRatingConfig());
        structureRepositoryConfigurator.configure(configuration.getCommon().getStructureDefinition());
        executionUnitRepositoryConfigurator.populateWith(configuration.getSpecific().getPopulation());
    }
}
