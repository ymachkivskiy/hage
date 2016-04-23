package org.hage.platform.component.structure.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.SingleRunnableStepPhase;
import org.hage.platform.component.structure.distribution.StructureChangeRemoteBuffer;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class StructureChangeDistributionStepPhase extends SingleRunnableStepPhase {

    @Autowired
    private StructureChangeRemoteBuffer structureChangeRemoteBuffer;

    @Override
    public String getPhaseName() {
        return "Structure change distribution";
    }

    @Override
    protected void executePhase(long currentStep) {
        structureChangeRemoteBuffer.onStepPerformed();
    }

}
