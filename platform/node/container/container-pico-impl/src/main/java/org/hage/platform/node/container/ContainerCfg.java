package org.hage.platform.node.container;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
public class ContainerCfg {

    @Bean
    public PicoInstanceContainer getComponentInstanceProvider() {
        return new PicoInstanceContainer();
    }

}
