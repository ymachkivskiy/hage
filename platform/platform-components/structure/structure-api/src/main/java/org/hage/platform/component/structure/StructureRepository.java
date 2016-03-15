package org.hage.platform.component.structure;

import org.hage.platform.component.structure.definition.Position;

import java.util.List;

public interface StructureRepository {

    boolean exists(Position position);

    boolean areNeighbors(Position first, Position second);

    List<Position> getNeighbors(Position position);
}
