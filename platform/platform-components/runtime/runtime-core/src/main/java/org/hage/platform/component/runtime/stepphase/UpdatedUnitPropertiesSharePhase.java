package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.stateprops.registry.PositionUnitProperties;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.component.runtime.stateprops.remote.UnitPropertiesSharingEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SingletonComponent
public class UpdatedUnitPropertiesSharePhase extends SingleRunnableStepPhase {

    @Autowired
    private UnitPropertiesSharingEndpoint unitPropertiesSharingEndpoint;
    @Autowired
    private UnitPropertiesRegistry unitPropertiesRegistry;

    @Override
    public String getPhaseName() {
        return "Share updated unit properties";
    }

    @Override
    protected void executePhase(long currentStep) {
        List<PositionUnitProperties> allUnitProperties = unitPropertiesRegistry.getUnitProperties();
        unitPropertiesSharingEndpoint.shareUpdatedProperties(allUnitProperties);
    }
}
