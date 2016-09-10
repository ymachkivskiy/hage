package org.hage.platform.util;

import org.hage.platform.util.bus.AsynchronousEventBus;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventBusRegisterPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusCfg {

    @Bean
    public EventBus getEventBus() {
        return new AsynchronousEventBus();
    }

    @Bean
    public EventBusRegisterPostProcessor getEventBusRegisterPostProcessor() {
        return new EventBusRegisterPostProcessor();
    }

}
