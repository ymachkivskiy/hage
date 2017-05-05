package org.hage.platform.node.runtime.location;

import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.connections.Neighbors;
import org.hage.platform.node.structure.connections.RelativePosition;

import java.util.*;

class NeighborsBuilder {

    private Map<Position, AgentsUnitAddress> addressMap = new HashMap<>();
    private Map<RelativePosition, Set<Position>> positioningMap = new EnumMap<>(RelativePosition.class);

    public void setRelativeNeighbors(RelativePosition relativePosition, List<Position> neighborsFor) {
        positioningMap.put(relativePosition, new HashSet<>(neighborsFor));
    }

    public void setPositionAddress(Position position, AgentsUnitAddress unitAddress) {
        addressMap.put(position, unitAddress);
    }

    public Neighbors build() {
        return new CachedNeighbors(addressMap, positioningMap);
    }

}
