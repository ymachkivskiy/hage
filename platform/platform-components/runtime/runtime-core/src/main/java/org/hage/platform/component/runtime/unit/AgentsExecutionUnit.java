package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.runtime.activepopulation.AgentsRunner;
import org.hage.platform.component.runtime.populationinit.UnitPopulationLoader;
import org.hage.platform.component.structure.Position;

public interface AgentsExecutionUnit {
    Position getPosition();

    void performPostProcessing();

    AgentsRunner getAgentsRunner();

    UnitPopulationLoader getUnitPopulationLoader();
}
