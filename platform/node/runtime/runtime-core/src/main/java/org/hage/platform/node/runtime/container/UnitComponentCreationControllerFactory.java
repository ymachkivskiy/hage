package org.hage.platform.node.runtime.container;

import org.hage.platform.node.runtime.activepopulation.AgentsTargetEnvironment;

public interface UnitComponentCreationControllerFactory {
    UnitComponentCreationController createControllerWithTargetEnv(AgentsTargetEnvironment unitActivePopulationCtrl);
}
