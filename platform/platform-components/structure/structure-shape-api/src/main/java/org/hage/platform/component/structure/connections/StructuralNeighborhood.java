package org.hage.platform.component.structure.connections;

import org.hage.platform.component.structure.Position;

import java.util.List;
import java.util.Set;

public interface StructuralNeighborhood {

    List<Position> getNeighborsFor(RelativePosition relativePosition);

    Set<Position> getAllNeighbors();

}
