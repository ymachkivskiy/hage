package org.hage.platform.component.lifecycle.distr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformLifecycleDistributedModuleConfiguration {

    @Bean
    public LifecycleManagerRemoteChanel lifecycleManagerRemoteChanel() {
        return new LifecycleManagerRemoteChanel();
    }

}
