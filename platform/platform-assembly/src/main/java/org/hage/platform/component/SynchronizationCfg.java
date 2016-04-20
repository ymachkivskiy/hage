package org.hage.platform.component;

import org.hage.platform.component.synchronization.DummySynchronizer;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SynchronizationCfg {

    @Bean
    public SynchronizationBarrier synchronizationBarrier() {
        return new DummySynchronizer();
    }

}
