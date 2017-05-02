package org.hage.platform.component.structure.distribution;

import org.hage.platform.component.structure.Position;

import java.util.Collection;

public interface StructureChangedDistributor {
    void updatePositions(Collection<Position> activated, Collection<Position> deactivated);
}
