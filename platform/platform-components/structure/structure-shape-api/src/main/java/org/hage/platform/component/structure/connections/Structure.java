package org.hage.platform.component.structure.connections;

import org.hage.platform.component.structure.Position;

public interface Structure {

    boolean belongsToStructure(Position position);

    boolean areNeighbors(Position first, Position second);

    StructuralNeighborhood getNeighborhoodOf(Position position);
}
