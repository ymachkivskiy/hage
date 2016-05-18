package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.phase.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.component.runtime.stateprops.remote.UnitPropertiesSharer;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class UpdatedUnitPropertiesSharePhase extends SingleRunnableStepPhase {

    @Autowired
    private UnitPropertiesSharer propertiesSharer;
    @Autowired
    private UnitPropertiesRegistry unitPropertiesRegistry;

    @Override
    public String getPhaseName() {
        return "Share updated unit properties";
    }

    @Override
    protected void executePhase(long currentStep) {
        propertiesSharer.shareUpdatedProperties(unitPropertiesRegistry.getUnitProperties());
    }
}
