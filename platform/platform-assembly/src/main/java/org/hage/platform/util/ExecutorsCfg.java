package org.hage.platform.util;

import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.hage.platform.util.executors.core.ParallelCoreBatchExecutor;
import org.hage.platform.util.executors.core.SimpleSequentialSameThreadCoreBatchExecutor;
import org.hage.platform.util.executors.simple.Worker;
import org.hage.platform.util.executors.simple.WorkerExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorsCfg {

    @Bean
    public WorkerExecutor workerExecutor() {
        return new Worker();
    }


    @Bean
    public CoreBatchExecutor coreBatchExecutor() {
        return new ParallelCoreBatchExecutor();
    }
}
