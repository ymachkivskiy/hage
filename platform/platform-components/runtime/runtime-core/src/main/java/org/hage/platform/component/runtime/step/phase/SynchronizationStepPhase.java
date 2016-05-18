package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.component.execution.step.phase.SingleRunnableStepPhase;
import org.hage.platform.component.synchronization.SynchPoint;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.Preconditions.checkArgument;

public class SynchronizationStepPhase extends SingleRunnableStepPhase {

    private final String phase;

    @Autowired
    private SynchronizationBarrier barrier;

    public SynchronizationStepPhase(String phase) {
        checkArgument(phase != null && phase.length() > 0, "Illegal phase name " + phase);
        this.phase = phase;
    }

    @Override
    public String getPhaseName() {
        return "Synchronization (" + phase + ")";
    }

    @Override
    protected void executePhase(long currentStep) {
        barrier.synchronizeOnStep(new SynchPoint(currentStep, phase));
    }

}
