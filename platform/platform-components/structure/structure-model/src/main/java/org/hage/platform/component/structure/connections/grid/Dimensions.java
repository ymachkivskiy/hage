package org.hage.platform.component.structure.connections.grid;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@ToString
public final class Dimensions implements Serializable {
    public static final Dimensions UNIT = definedBy(1, 1, 1);

    public final int depthSize;
    public final int horizontalSize;
    public final int verticalSize;

    public static Dimensions definedBy(int depthSize, int horizontalSize, int verticalSize) {
        checkArgument(depthSize > 0);
        checkArgument(horizontalSize > 0);
        checkArgument(verticalSize > 0);

        return new Dimensions(depthSize, horizontalSize, verticalSize);
    }
}
