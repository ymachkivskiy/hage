package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.component.runtime.stateprops.remote.UnitPropertiesSharer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.PRE__SHARE_UPDATED_UNIT_PROPERTIES;

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
