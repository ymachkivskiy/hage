package org.hage.platform.util;

import org.hage.platform.node.executors.simple.Worker;
import org.hage.platform.node.executors.simple.WorkerExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorsCfg {

    @Bean
    public WorkerExecutor workerExecutor() {
        return new Worker();
    }

}
