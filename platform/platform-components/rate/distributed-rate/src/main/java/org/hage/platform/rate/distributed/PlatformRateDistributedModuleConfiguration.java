package org.hage.platform.rate.distributed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformRateDistributedModuleConfiguration.class)
public class PlatformRateDistributedModuleConfiguration {

    @Bean
    public ClusterPerformanceManager getClusterPerformanceManager() {
        return new CombinedClusterPerformanceManager();
    }

}
