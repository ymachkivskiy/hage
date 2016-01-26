package org.hage.platform.util.bus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ModuleConfiguration.class)
class ModuleConfiguration {

    @Bean
    public EventBus getEventBus() {
        return new SynchronousEventBus();
    }

}
