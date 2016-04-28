package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.runtime.unit.faces.AgentMigrationTarget;
import org.hage.platform.component.runtime.unit.faces.AgentsRunner;
import org.hage.platform.component.runtime.unit.faces.UnitPopulationLoader;
import org.hage.platform.component.structure.Position;

public interface Unit {
    Position getPosition();

    AgentsRunner asAgentsRunner();

    UnitPopulationLoader asUnitPopulationLoader();

    AgentMigrationTarget asAgentMigrationTarget();

    void postProcess();
}
