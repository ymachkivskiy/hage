package org.hage.platform.component.execution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.IndependentPhasesGroup;
import org.hage.platform.component.execution.step.StepPhaseFactory;
import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

@Slf4j
@SingletonComponent
class ExecutionStepRunnable implements Runnable {

    @Autowired
    private StepPhaseFactory stepPhaseFactory;
    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    private final AtomicLong stepsPerformed = new AtomicLong(0);

    @Override
    public void run() {

        log.debug("Started performing step {}", getCurrentStepNumber());

        stepPhaseFactory.getFullCyclePhasesGroups().forEach(this::executePhasesGroup);

        log.debug("Finished performing step {}", getCurrentStepNumber());

        stepsPerformed.incrementAndGet();
    }

    public long getPerformedStepsCount() {
        return stepsPerformed.get();
    }

    public void reset() {
        log.debug("Perform reset of task");

        stepsPerformed.set(0);
    }

    private long getCurrentStepNumber() {
        return stepsPerformed.get() + 1;
    }

    private void executePhasesGroup(IndependentPhasesGroup group) {
        long currentStepNumber = getCurrentStepNumber();

        List<Runnable> a = group.getPhases()
            .stream()
            .map(p -> p.getRunnable(currentStepNumber))
            .flatMap(Collection::stream)
            .collect(toList());

        coreBatchExecutor.executeAll(a);
    }

}
