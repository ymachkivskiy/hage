package org.hage.platform.habitat.structure;

import lombok.Data;

@Data(staticConstructor = "of")
public final class Dimensions {
    public static final Dimensions UNIT = of(1, 1, 1);

    private final int xDim;
    private final int yDim;
    private final int zDim;
}
