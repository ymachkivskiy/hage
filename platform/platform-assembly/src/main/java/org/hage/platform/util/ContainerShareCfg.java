package org.hage.platform.util;

import org.hage.platform.util.container.share.SharedComponentsPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerShareCfg {

    @Bean
    public SharedComponentsPostProcessor getSharedComponentsPostProcessor() {
        return new SharedComponentsPostProcessor();
    }

}
