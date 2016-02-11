package org.hage.platform.config.distribution;

import org.hage.platform.config.distribution.division.ConfigurationSplitAllocator;
import org.hage.platform.config.distribution.division.PerformanceProportionsBasedConfigurationSplitAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformConfigDistributedModuleConfiguration.class)
public class PlatformConfigDistributedModuleConfiguration {

    @Bean
    public ConfigurationSplitAllocator getConfigurationSplitAllocator() {
        return new PerformanceProportionsBasedConfigurationSplitAllocator();
    }

    @Bean
    public ConfigurationDistributor configurationDistributor() {
        return new ConfigurationDistributorImpl();
    }

}
