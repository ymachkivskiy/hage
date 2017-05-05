package org.hage.platform.node.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.phase.ExecutionPhaseType;
import org.hage.platform.node.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.node.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.node.runtime.stateprops.remote.UnitPropertiesSharer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.node.execution.phase.ExecutionPhaseType.PRE__SHARE_UPDATED_UNIT_PROPERTIES;

@SingletonComponent
public class UpdatedUnitPropertiesSharePhase extends SingleTaskExecutionPhase {

    @Autowired
    private UnitPropertiesSharer propertiesSharer;
    @Autowired
    private UnitPropertiesRegistry unitPropertiesRegistry;

    @Override
    public ExecutionPhaseType getType() {
        return PRE__SHARE_UPDATED_UNIT_PROPERTIES;
    }

    @Override
    protected void execute(long currentStep) {
        propertiesSharer.shareUpdatedProperties(unitPropertiesRegistry.getUnitProperties());
    }
}
