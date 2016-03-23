package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Position;

import java.util.List;

public interface GridNeighborhoodGenerator {
    List<Position> generateNeighbors(Position center);
}
