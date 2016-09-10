package org.hage.platform.util.executors.core;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.util.executors.config.internal.ThreadingPolicyInternalConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;

@SingletonComponent
class CoreBatchExecutorImpl implements CoreBatchExecutor {

    private ExecutionEngine engine;

    @Autowired
    private ExecutionEngineFactory executionEngineFactory;
    @Autowired
    private ThreadingPolicyInternalConfigurationProvider threadingPolicyConfigurationProvider;

    @PostConstruct
    private void initialize() {
        engine = executionEngineFactory.createExecutionEngine(threadingPolicyConfigurationProvider.getThreadingPolicyConfiguration());
    }

    @Override
    public void executeAll(Collection<? extends Runnable> tasks) {
        engine.executeAll(tasks);
    }
}
