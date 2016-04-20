package org.hage.platform.component;

import org.hage.platform.component.synchronization.SynchronizationEndpoint;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SynchronizationCfg {

    @Bean
    public SynchronizationEndpoint synchronizationBarrier() {
        return new SynchronizationEndpoint();
    }

}
