package org.hage.platform.component.execution.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

@Slf4j
@SingletonComponent
public class ExecutionStep implements ExecutionStepRunnable, ResetableStepRunnable {

    @Autowired
    private StepPhaseFactory stepPhaseFactory;
    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    private final AtomicLong stepsPerformed = new AtomicLong(0);

    @Override
    public void run() {

        log.debug("### Started performing step {}", getCurrentStepNumber());

        stepPhaseFactory.getFullCyclePhasesGroups().forEach(this::executePhasesGroup);

        log.debug("### Finished performing step {}", getCurrentStepNumber());

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

    private void executePhasesGroup(IndependentPhasesGroup phasesGroup) {
        log.info("--- Start executing group of phases {} in step {} --- ", phasesGroup, getCurrentStepNumber());

        coreBatchExecutor.executeAll(toTasks(phasesGroup));

        log.info("--- Finish executing group of phases {} in step {} ---", phasesGroup, getCurrentStepNumber());
    }

    private List<Runnable> toTasks(IndependentPhasesGroup group) {
        final long currentStep = getCurrentStepNumber();
        return group.getPhases()
                .stream()
                .map(p -> p.getRunnable(currentStep))
                .flatMap(Collection::stream)
                .collect(toList());
    }

}
