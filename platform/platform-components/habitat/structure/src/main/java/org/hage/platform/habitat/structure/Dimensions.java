package org.hage.platform.habitat.structure;

import lombok.Data;

import java.io.Serializable;

@Data(staticConstructor = "of")
public final class Dimensions implements Serializable {
    public static final Dimensions UNIT = of(1, 1, 1);

    private final int xDim;
    private final int yDim;
    private final int zDim;
}
