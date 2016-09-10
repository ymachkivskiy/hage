package org.hage.platform.util.executors.core;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.util.executors.config.internal.ThreadingPolicyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@SingletonComponent
class ExecutionEngineFactory {

    @Autowired(required = false)
    private List<ThreadingPolicyEngineCreationStrategy> creationStrategies = emptyList();

    public ExecutionEngine createExecutionEngine(ThreadingPolicyConfiguration configuration) {
        log.debug("Creating execution engine using configuration {}", configuration);

        for (ThreadingPolicyEngineCreationStrategy strategy : creationStrategies) {
            if (strategy.supports(configuration)) {
                return strategy.create(configuration);
            }
        }

        log.error("Threading policy configuration {} is not supported", configuration);

        throw new HageRuntimeException("Threading policy configuration " + configuration + " is not supported");
    }

}
