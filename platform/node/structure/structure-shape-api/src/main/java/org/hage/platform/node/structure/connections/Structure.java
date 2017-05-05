package org.hage.platform.node.structure.connections;

import org.hage.platform.node.structure.Position;

public interface Structure {

    boolean belongsToStructure(Position position);

    boolean areNeighbors(Position first, Position second);

    StructuralNeighborhood getNeighborhoodOf(Position position);
}
