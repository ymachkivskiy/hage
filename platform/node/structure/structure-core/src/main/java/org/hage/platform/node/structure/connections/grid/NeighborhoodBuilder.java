package org.hage.platform.node.structure.connections.grid;

import org.hage.platform.node.structure.connections.StructuralNeighborhood;
import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.connections.RelativePosition;

import java.util.EnumMap;
import java.util.List;

import static org.hage.util.CollectionUtils.nullSafeCopy;

class NeighborhoodBuilder {

    private EnumMap<RelativePosition, List<Position>> neighborsMap = new EnumMap<>(RelativePosition.class);

    public static NeighborhoodBuilder neighborhoodBuilder() {
        return new NeighborhoodBuilder();
    }

    public NeighborhoodBuilder withNeighborsFor(RelativePosition relativePosition, List<Position> neighbors) {
        neighborsMap.put(relativePosition, nullSafeCopy(neighbors));
        return this;
    }

    public StructuralNeighborhood build() {
        return new GridStructuralNeighborhood(neighborsMap);
    }

}
