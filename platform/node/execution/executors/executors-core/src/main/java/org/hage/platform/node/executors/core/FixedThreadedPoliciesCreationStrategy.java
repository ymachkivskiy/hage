package org.hage.platform.node.executors.core;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyConfiguration;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyType;

import static java.lang.Runtime.getRuntime;
import static org.hage.platform.node.executors.config.internal.ThreadingPolicyType.ADJUSTED_TO_PROCESSORS_NUMBER;
import static org.hage.platform.node.executors.config.internal.ThreadingPolicyType.FIXED_THREADS_NUMBER;

@Slf4j
@SingletonComponent
class FixedThreadedPoliciesCreationStrategy implements ThreadingPolicyEngineCreationStrategy {

    @Override
    public boolean supports(ThreadingPolicyConfiguration configuration) {

        if (configuration != null) {
            ThreadingPolicyType policyType = configuration.getPolicyType();
            return policyType == ADJUSTED_TO_PROCESSORS_NUMBER || (policyType == FIXED_THREADS_NUMBER && configuration.getFixedNumber() != null);
        }

        return false;
    }

    @Override
    public ExecutionEngine create(ThreadingPolicyConfiguration configuration) {
        int availableProcessors = getRuntime().availableProcessors();


        if (configuration.getPolicyType() == ADJUSTED_TO_PROCESSORS_NUMBER) {
            log.debug("Creating execution engine with same number of threads ({}) as there are processors available", availableProcessors);

            return new FixedThreadsNumberExecutorEngine(availableProcessors);
        } else {
            Integer numberOfRequestedThreads = configuration.getFixedNumber();
            log.debug("Creating execution engine with fixed number of threads ({})", numberOfRequestedThreads);

            if (numberOfRequestedThreads > availableProcessors) {
                log.warn("Creating execution engine with number of threads ({}) greater than available processors ({}), which may slow down execution", numberOfRequestedThreads, availableProcessors);
            }

            return new FixedThreadsNumberExecutorEngine(numberOfRequestedThreads);
        }

    }
}
