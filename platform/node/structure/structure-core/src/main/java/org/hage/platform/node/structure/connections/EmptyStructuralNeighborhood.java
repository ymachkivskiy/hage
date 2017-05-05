package org.hage.platform.node.structure.connections;

import lombok.RequiredArgsConstructor;
import org.hage.platform.node.structure.Position;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class EmptyStructuralNeighborhood implements StructuralNeighborhood {
    public static final EmptyStructuralNeighborhood INSTANCE = new EmptyStructuralNeighborhood();

    @Override
    public List<Position> getNeighborsFor(RelativePosition relativePosition) {
        return emptyList();
    }

    @Override
    public Set<Position> getAllNeighbors() {
        return emptySet();
    }

}
