package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.SingleRunnableStepPhase;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class SynchronizationStepPhase extends SingleRunnableStepPhase {

    @Autowired
    private SynchronizationBarrier barrier;

    @Override
    public String getPhaseName() {
        return "Synchronization";
    }

    @Override
    protected void executePhase(long currentStep) {
        barrier.synchronizeOnStep(currentStep);
    }

}
