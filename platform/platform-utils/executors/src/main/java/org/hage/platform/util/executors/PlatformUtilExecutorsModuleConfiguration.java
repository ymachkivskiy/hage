package org.hage.platform.util.executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformUtilExecutorsModuleConfiguration {

    @Bean
    public WorkerExecutor workerExecutor() {
        return new Worker();
    }

}
