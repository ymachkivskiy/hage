package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.SYNC;
import static org.hage.platform.component.synchronization.SynchPoint.stepPointSubphase;

@SingletonComponent
public class BeforeAgentsRunSynchronizationPhase extends SingleTaskExecutionPhase {

    @Autowired
    private SynchronizationBarrier barrier;

    @Override
    public ExecutionPhaseType getType() {
        return SYNC;
    }

    @Override
    protected void execute(long currentStep) {
        barrier.synchronize(stepPointSubphase(currentStep, "before-agents-run"));
    }

}
