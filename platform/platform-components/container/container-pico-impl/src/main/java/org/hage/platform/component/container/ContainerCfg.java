package org.hage.platform.component.container;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@PlugableConfiguration
public class ContainerCfg {

    @Bean
    public PicoInstanceContainer getComponentInstanceProvider() {
        return new PicoInstanceContainer();
    }

}
