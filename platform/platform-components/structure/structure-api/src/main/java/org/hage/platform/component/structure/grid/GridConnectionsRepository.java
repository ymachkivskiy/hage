package org.hage.platform.component.structure.grid;

import org.hage.platform.component.structure.definition.Position;

public interface GridConnectionsRepository {

    boolean areNeighbors(Position first, Position second);

    Neighbors getNeighbors(Position position);
}
