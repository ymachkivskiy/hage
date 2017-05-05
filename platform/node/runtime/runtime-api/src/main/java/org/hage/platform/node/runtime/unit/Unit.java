package org.hage.platform.node.runtime.unit;

import org.hage.platform.node.structure.Position;

public interface Unit extends AgentsRunner, UnitPopulationLoader, AgentMigrationTarget, StateChangePerformer {
    Position getPosition();

    void postProcess();
}
