package org.hage.platform.node.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.phase.ExecutionPhaseType;
import org.hage.platform.node.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.cluster.synch.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.node.execution.phase.ExecutionPhaseType.SYNC;
import static org.hage.platform.cluster.synch.SynchPoint.stepPointSubphase;

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
