package org.hage.platform.component.execution.core;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.core.executor.ExecutionPhasesProvider;
import org.hage.platform.component.execution.core.executor.PhasedExecutor;
import org.hage.platform.component.execution.core.supplier.TasksChange;
import org.hage.platform.component.execution.core.supplier.TasksChangeSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ExecutionStepRunnableAdapter implements Runnable {

    @Autowired
    private PhasedExecutor phasedExecutor;
    @Autowired
    private TasksChangeSupplier tasksChangeSupplier;
    @Autowired
    private ExecutionPhasesProvider phasesProvider;

    private long stepsPerformed = 0;

    @Override
    public void run() {
        log.info("Start performing step: {}", stepsPerformed + 1);

        TasksChange tasksChange = tasksChangeSupplier.pollCurrentChange();
        phasedExecutor.register(tasksChange.getToBeAdded());
        phasedExecutor.unregister(tasksChange.getToBeRemoved());

        phasedExecutor.performFullCycle(phasesProvider.getExecutionPhasesInOrder());

        ++stepsPerformed;

        log.info("Finish performing step: {}", stepsPerformed);
    }

    public void reset() {
        stepsPerformed = 0;
        phasedExecutor.unregisterAll();
    }

    public long getPerformedStepsCount() {
        return stepsPerformed;
    }

}
