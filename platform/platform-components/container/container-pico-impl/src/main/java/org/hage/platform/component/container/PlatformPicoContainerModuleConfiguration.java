package org.hage.platform.component.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformPicoContainerModuleConfiguration {

    @Bean
    public PicoInstanceContainer getComponentInstanceProvider() {
        return new PicoInstanceContainer();
    }

}
