package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

class MockedLoader implements ConfigurationLoader {

    @Autowired
    private Config1a_NoLoadBalancedMigrAgents supplier;


    @Override
    public InputConfiguration load() throws ConfigurationNotFoundException {
        return supplier.getConfiguration();
    }


}
