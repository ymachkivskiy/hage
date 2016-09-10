package org.hage.platform.util.executors.core;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.util.executors.config.internal.ThreadingPolicyConfiguration;

import java.util.Collection;

import static org.hage.platform.util.executors.config.internal.ThreadingPolicyType.DIRECT_EXECUTION;

@SingletonComponent
class DirectThreadingPolicyCreationStrategy implements ThreadingPolicyEngineCreationStrategy {

    @Override
    public boolean supports(ThreadingPolicyConfiguration configuration) {
        return configuration != null && configuration.getPolicyType() == DIRECT_EXECUTION;
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
