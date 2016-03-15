package org.hage.platform.component.structure.definition;

import lombok.Data;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@Data(staticConstructor = "definedBy")
public final class Position implements Serializable {
    public static final Position ZERO = definedBy(0, 0, 0);

    private final int x;
    private final int y;
    private final int z;

    public Position shift(int xShift, int yShift, int zShift) {
        return definedBy(x + xShift, y + yShift, z + zShift);
    }
}
