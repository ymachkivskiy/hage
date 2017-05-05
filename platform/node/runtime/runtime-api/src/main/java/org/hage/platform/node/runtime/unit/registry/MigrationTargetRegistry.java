package org.hage.platform.node.runtime.unit.registry;

import org.hage.platform.node.runtime.unit.AgentMigrationTarget;
import org.hage.platform.node.structure.Position;

public interface MigrationTargetRegistry {
    AgentMigrationTarget migrationTargetFor(Position position);
}
