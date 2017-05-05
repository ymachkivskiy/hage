package org.hage.platform.util;

import org.hage.platform.node.bus.AsynchronousEventBus;
import org.hage.platform.node.bus.EventBus;
import org.hage.platform.node.bus.EventBusRegisterPostProcessor;
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
