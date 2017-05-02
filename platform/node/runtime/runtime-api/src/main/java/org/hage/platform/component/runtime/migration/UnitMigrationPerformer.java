package org.hage.platform.component.runtime.migration;

import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.Position;

import java.util.List;

public interface UnitMigrationPerformer {
    void migrateUnits(List<Position> unitsPositions, NodeAddress targetNode);
}
