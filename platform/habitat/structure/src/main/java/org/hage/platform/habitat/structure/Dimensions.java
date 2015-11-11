package org.hage.platform.habitat.structure;

import lombok.Data;

@Data(staticConstructor = "of")
public final class Dimensions {
    private final int xDim;
    private final int yDim;
    private final int zDim;
}
