package org.hage.platform.component.structure.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.component.structure.distribution.StructureChangeRemoteBuffer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.PRE__STRUCTURE_DISTRIBUTION;

@SingletonComponent
public class StructureChangeDistributionStepPhase extends SingleTaskExecutionPhase {

    @Autowired
    private StructureChangeRemoteBuffer structureChangeRemoteBuffer;

    @Override
    public ExecutionPhaseType getType() {
        return PRE__STRUCTURE_DISTRIBUTION;
    }

    @Override
    protected void execute(long currentStep) {
        structureChangeRemoteBuffer.onStepPerformed();
    }

}
