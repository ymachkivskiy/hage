package org.hage.platform.component.runtime.unit.registry;

import org.hage.platform.component.runtime.unit.faces.AgentMigrationTarget;
import org.hage.platform.component.structure.Position;

public interface MigrationTargetRegistry {
    AgentMigrationTarget migrationTargetFor(Position position);
}
