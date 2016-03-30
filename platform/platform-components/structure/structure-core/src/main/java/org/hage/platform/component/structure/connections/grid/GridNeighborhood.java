package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Neighborhood;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.RelativePosition;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toSet;

class GridNeighborhood implements Neighborhood {

    private final Map<RelativePosition, List<Position>> neighborhoodMap;

    GridNeighborhood(Map<RelativePosition, List<Position>> neighborhoodMap) {
        this.neighborhoodMap = unmodifiableMap(new EnumMap<>(neighborhoodMap));
    }

    @Override
    public List<Position> getNeighborsFor(RelativePosition relativePosition) {
        return neighborhoodMap.getOrDefault(relativePosition, emptyList());
    }

    @Override
    public Set<Position> getAllNeighbors() {
        return neighborhoodMap.values().stream()
            .flatMap(List::stream)
            .collect(toSet());
    }
}
