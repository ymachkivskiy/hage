package org.hage.platform.component.runtime.execution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.execution.change.TasksChange;
import org.hage.platform.component.runtime.execution.change.TasksChangeSupplier;
import org.hage.platform.component.runtime.execution.cycle.PostStepPhasesRunner;
import org.hage.platform.component.runtime.execution.phase.ExecutionPhasesProvider;
import org.hage.platform.component.runtime.execution.phase.PhasedExecutor;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.util.CollectionUtils.nullSafe;

@SingletonComponent
@Slf4j
public class ExecutionStepAdapter implements Runnable {

    @Autowired
    private PhasedExecutor phasedExecutor;
    @Autowired
    private TasksChangeSupplier tasksChangeSupplier;
    @Autowired
    private ExecutionPhasesProvider phasesProvider;
    @Autowired
    private PostStepPhasesRunner postStepPhasesRunner;
    @Autowired
    private SynchronizationBarrier barrier;

    private long stepsPerformed = 0;

    @Override
    public void run() {
        log.info("Start performing step: {}", stepsPerformed + 1);

        updateTasksList();

        performStep();

        notifyStepPerformed();

        barrier.synchronize();

        log.info("Finish performing step: {}", stepsPerformed);
    }

    public void reset() {
        stepsPerformed = 0;
        phasedExecutor.unregisterAll();
    }

    public long getPerformedStepsCount() {
        return stepsPerformed;
    }

    private void updateTasksList() {
        TasksChange tasksChange = tasksChangeSupplier.pollCurrentChange();
        phasedExecutor.register(tasksChange.getToBeAdded());
        phasedExecutor.unregister(tasksChange.getToBeRemoved());
    }

    private void performStep() {
        phasedExecutor.performFullCycle(phasesProvider.getExecutionPhasesInOrder());
        ++stepsPerformed;
    }

    private void notifyStepPerformed() {
        postStepPhasesRunner.stepPerformed(stepsPerformed);
    }

}
