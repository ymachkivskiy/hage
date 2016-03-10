package org.hage.platform.rate.distributed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformRateDistributedModuleConfiguration {

    @Bean
    public ClusterPerformanceManager getClusterPerformanceManager() {
        return new ClusterPerformanceManagerEndpoint();
    }

}
