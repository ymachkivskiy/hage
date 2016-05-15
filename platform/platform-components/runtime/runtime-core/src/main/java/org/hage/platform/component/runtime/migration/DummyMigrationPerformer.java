package org.hage.platform.component.runtime.migration;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.Position;

import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@SingletonComponent
@Slf4j
public class DummyMigrationPerformer implements UnitMigrationPerformer {

    @Override
    public void migrateUnits(List<Position> unitsPositions, NodeAddress targetNode) {
        log.debug("Migrate units {} to node {}", nullSafe(unitsPositions), targetNode);


    }

}
