package org.hage.platform.component.structure;

import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.definition.Position;

import java.util.List;
import java.util.Optional;

public class StructureManager implements StructureRepository {


    @Override
    public boolean areNeighbors(Position first, Position second) {
        return false;
    }

    @Override
    public List<Position> getNeighbors(Position position) {
        return null;
    }

    @Override
    public Optional<NodeAddress> getAddressFor(Position position) {
        return null;
    }
}
