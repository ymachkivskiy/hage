package org.hage.platform.component.execution.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.ExecutionPhaseFactory;
import org.hage.platform.component.execution.phase.StepPhaseExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SingletonComponent
class ExecutionStepTask implements ExecutionStepInfoProvider, StepTask {

    @Autowired
    private ExecutionPhaseFactory stepPhaseFactory;
    @Autowired
    private StepPhaseExecutor stepPhaseExecutor;

    private final AtomicLong currentStep = new AtomicLong(1);

    @Override
    public long getPerformedStepsCount() {
        return currentStep.get() - 1;
    }

    @Override
    public long getCurrentStepNumber() {
        return currentStep.get();
    }

    @Override
    public boolean perform() {

        long currentStepNumber = currentStep.get();

        log.info("############# Started performing step {} #############", currentStepNumber);

        for (ExecutionPhase executionPhase : stepPhaseFactory.getFullCyclePhases()) {
            stepPhaseExecutor.executeStepPhase(executionPhase, currentStepNumber);
        }

        log.info("############# Finished performing step {} #############", currentStepNumber);

        currentStep.incrementAndGet();

        return true;
    }

    @Override
    public void reset() {
        log.debug("Perform reset of task");
        currentStep.set(1);
    }

}
