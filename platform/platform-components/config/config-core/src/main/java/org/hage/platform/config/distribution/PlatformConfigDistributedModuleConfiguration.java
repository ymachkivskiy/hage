package org.hage.platform.config.distribution;

import org.hage.platform.component.config.ConfigurationDistributor;
import org.hage.platform.config.distribution.division.ConfigurationAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformConfigDistributedModuleConfiguration.class)
class PlatformConfigDistributedModuleConfiguration {

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
