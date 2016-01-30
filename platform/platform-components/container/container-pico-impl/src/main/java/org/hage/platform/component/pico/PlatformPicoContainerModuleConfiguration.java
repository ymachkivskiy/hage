package org.hage.platform.component.pico;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformPicoContainerModuleConfiguration {

    @Bean
    public PicoComponentInstanceProvider getComponentInstanceProvider() {
        return new PicoComponentInstanceProvider();
    }

}
