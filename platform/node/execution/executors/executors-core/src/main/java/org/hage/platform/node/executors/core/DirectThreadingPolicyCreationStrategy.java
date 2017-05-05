package org.hage.platform.node.executors.core;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyConfiguration;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyType;

import java.util.Collection;

@SingletonComponent
class DirectThreadingPolicyCreationStrategy implements ThreadingPolicyEngineCreationStrategy {

    @Override
    public boolean supports(ThreadingPolicyConfiguration configuration) {
        return configuration != null && configuration.getPolicyType() == ThreadingPolicyType.DIRECT_EXECUTION;
    }

    @Override
    public ExecutionEngine create(ThreadingPolicyConfiguration configuration) {
        return new DirectExecutionEngine();
    }

    private static class DirectExecutionEngine implements ExecutionEngine {

        @Override
        public void executeAll(Collection<? extends Runnable> tasks) {
            for (Runnable task : tasks) {
                task.run();
            }
        }

    }

}
