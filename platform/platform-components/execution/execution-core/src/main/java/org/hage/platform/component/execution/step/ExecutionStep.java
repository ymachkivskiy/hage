package org.hage.platform.component.execution.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.ExecutionPhaseFactory;
import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SingletonComponent
public class ExecutionStep implements ExecutionStepRunnable, ResetableStepRunnable {

    @Autowired
    private ExecutionPhaseFactory stepPhaseFactory;
    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    private final AtomicLong stepsPerformed = new AtomicLong(0);

    @Override
    public void run() {

        log.info("############# Started performing step {} #############", getCurrentStepNumber());

        stepPhaseFactory.getFullCyclePhases().forEach(this::executePhase);

        log.info("############# Finished performing step {} #############", getCurrentStepNumber());

        stepsPerformed.incrementAndGet();
    }

    @Override
    public long getPerformedStepsCount() {
        return stepsPerformed.get();
    }

    @Override
    public long getCurrentStepNumber() {
        return stepsPerformed.get() + 1;
    }

    public void reset() {
        log.debug("Perform reset of task");
        stepsPerformed.set(0);
    }

    private void executePhase(ExecutionPhase executionPhase) {
        log.info("   ------ Start executing phase \"{}\" in step [{}] -------   ", executionPhase.getType().getDescription(), getCurrentStepNumber());

        coreBatchExecutor.executeAll(executionPhase.getTasks(getCurrentStepNumber()));

        log.info("   ------ Finish executing phase \"{}\" in step [{}] ------   ", executionPhase.getType().getDescription(), getCurrentStepNumber());
    }

}
