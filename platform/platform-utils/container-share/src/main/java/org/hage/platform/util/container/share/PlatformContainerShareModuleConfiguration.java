package org.hage.platform.util.container.share;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformContainerShareModuleConfiguration {

    @Bean
    public SharedComponentsPostProcessor getSharedComponentsPostProcessor() {
        return new SharedComponentsPostProcessor();
    }

}
