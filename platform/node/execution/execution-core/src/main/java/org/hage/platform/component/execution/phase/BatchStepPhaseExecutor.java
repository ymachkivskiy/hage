package org.hage.platform.component.execution.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SingletonComponent
class BatchStepPhaseExecutor implements StepPhaseExecutor {

    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    @Override
    public void executeStepPhase(ExecutionPhase executionPhase, long stepNumber) {
        coreBatchExecutor.executeAll(executionPhase.getTasks(stepNumber));
    }

}
