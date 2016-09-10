package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.structure.Position;

public interface Unit extends AgentsRunner, UnitPopulationLoader, AgentMigrationTarget, StateChangePerformer {
    Position getPosition();

    void postProcess();
}
