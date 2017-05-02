package org.hage.platform.component.runtime.container;

import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;

public interface UnitComponentCreationControllerFactory {
    UnitComponentCreationController createControllerWithTargetEnv(AgentsTargetEnvironment unitActivePopulationCtrl);
}
