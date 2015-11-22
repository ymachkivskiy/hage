package org.hage.platform.habitat.structure;

import lombok.Data;

import java.util.Set;

import static java.util.Collections.emptySet;

@Data
public final class Chunk {
    private final InternalPosition coordinatesStartPosition;
    private final Dimensions dimensions;

    public Long getSize() {
        return ((long) dimensions.getXDim()) * dimensions.getYDim() * dimensions.getZDim();
    }

    public boolean containsPosition(InternalPosition position) {
        return false;
    }

    public Set<InternalPosition> getInternalPositions() {
        return emptySet();
    }
}
