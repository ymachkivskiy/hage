package org.hage.platform.node.runtime.migration;

import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.structure.Position;

import java.util.List;

public interface UnitMigrationPerformer {
    void migrateUnits(List<Position> unitsPositions, NodeAddress targetNode);
}
