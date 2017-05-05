package org.hage.platform.component;

import org.hage.platform.simconf.ConfigurationDistributor;
import org.hage.platform.simconf.PerformanceConfigurationDistributor;
import org.hage.platform.simconf.SimulationConfigurationHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimulationConfigurationCoreCfg {

    @Bean
    public ConfigurationDistributor configurationDistributor() {
        return new PerformanceConfigurationDistributor();
    }

    @Bean
    public SimulationConfigurationHub simulationConfigurationHub() {
        return new SimulationConfigurationHub();
    }
}
