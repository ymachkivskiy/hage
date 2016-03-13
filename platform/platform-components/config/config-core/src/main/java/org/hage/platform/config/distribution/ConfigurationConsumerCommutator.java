package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationConsumer;
import org.hage.platform.component.rate.RateConfigurationConsumer;
import org.hage.platform.component.simulation.structure.SimulationOrganizationConsumer;
import org.hage.platform.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ConfigurationConsumerCommutator implements ConfigurationConsumer {

    @Autowired
    private SimulationOrganizationConsumer simulationOrganizationConsumer;
    @Autowired
    private RateConfigurationConsumer rateConfigurationConsumer;


    @Override
    public void acceptConfiguration(Configuration configuration) {
        log.debug("Accepting configuration '{}'",configuration);

        rateConfigurationConsumer.acceptRateConfiguration(configuration.getCommon().getRatingConfig());
        simulationOrganizationConsumer.acceptOrganizationConfiguration(configuration.getSpecific().getSimulationOrganization());
    }
}
