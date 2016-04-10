package org.hage.platform.component;

import org.hage.platform.component.simulationconfig.ClusterPerformanceBasedConfigurationDistributor;
import org.hage.platform.component.simulationconfig.ConfigurationDistributor;
import org.hage.platform.component.simulationconfig.SimulationConfigurationHub;
import org.hage.platform.component.simulationconfig.division.ConfigurationAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimulationConfigurationCoreCfg {

    @Bean
    public ConfigurationAllocator getConfigurationSplitAllocator() {
        return new ConfigurationAllocator();
    }

    @Bean
    public ConfigurationDistributor configurationDistributor() {
        return new ClusterPerformanceBasedConfigurationDistributor();
    }

    @Bean
    public SimulationConfigurationHub simulationConfigurationHub() {
        return new SimulationConfigurationHub();
    }
}
