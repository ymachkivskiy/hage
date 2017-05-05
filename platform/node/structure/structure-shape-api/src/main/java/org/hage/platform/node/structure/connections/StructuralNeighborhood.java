package org.hage.platform.node.structure.connections;

import org.hage.platform.node.structure.Position;

import java.util.List;
import java.util.Set;

public interface StructuralNeighborhood {

    List<Position> getNeighborsFor(RelativePosition relativePosition);

    Set<Position> getAllNeighbors();

}
