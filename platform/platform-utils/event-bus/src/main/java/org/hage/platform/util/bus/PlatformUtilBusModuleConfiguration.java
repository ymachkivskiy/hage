package org.hage.platform.util.bus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformUtilBusModuleConfiguration.class)
class PlatformUtilBusModuleConfiguration {

    @Bean
    public EventBus getEventBus() {
        return new SynchronousEventBus();
    }

}
