package org.hage.platform.component.structure;

import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.definition.Position;

import java.util.List;
import java.util.Optional;

public interface StructureRepository {

    boolean areNeighbors(Position first, Position second);

    List<Position> getNeighbors(Position position);

    Optional<NodeAddress> getAddressFor(Position position);

}
