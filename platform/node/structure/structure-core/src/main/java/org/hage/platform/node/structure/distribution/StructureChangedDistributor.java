package org.hage.platform.node.structure.distribution;

import org.hage.platform.node.structure.Position;

import java.util.Collection;

public interface StructureChangedDistributor {
    void updatePositions(Collection<Position> activated, Collection<Position> deactivated);
}
