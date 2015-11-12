package org.hage.platform.habitat.structure;

import lombok.Data;

@Data
public final class Chunk {
    private final InternalPosition coordinatesStartPosition;
    private final Dimensions dimensions;

    public Long getSize() {
        return ((long) dimensions.getXDim()) * dimensions.getYDim() * dimensions.getZDim();
    }
}
