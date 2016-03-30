package org.hage.platform.component.structure.connections;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class EmptyNeighborhood implements Neighborhood {
    public static final EmptyNeighborhood INSTANCE = new EmptyNeighborhood();

    @Override
    public List<Position> getNeighborsFor(RelativePosition relativePosition) {
        return emptyList();
    }

    @Override
    public Set<Position> getAllNeighbors() {
        return emptySet();
    }

}
