package org.hage.platform.config.distributed;

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

}
