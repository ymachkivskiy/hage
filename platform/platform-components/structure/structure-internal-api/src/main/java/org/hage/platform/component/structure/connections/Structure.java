package org.hage.platform.component.structure.connections;

public interface Structure {

    boolean belongsToStructure(Position position);

    boolean areNeighbors(Position first, Position second);

    Neighborhood getNeighborhoodOf(Position position);
}
