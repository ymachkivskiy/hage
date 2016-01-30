package org.hage.platform.util.bus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformUtilBusModuleConfiguration {

    @Bean
    public EventBus getEventBus() {
        return new AsynchronousEventBus();
    }

    @Bean
    public EventBusRegisterPostProcessor getEventBusRegisterPostProcessor() {
        return new EventBusRegisterPostProcessor();
    }

}
