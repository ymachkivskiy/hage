package org.hage.platform.component.structure.definition;

import lombok.Data;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@Data(staticConstructor = "definedBy")
public final class Position implements Serializable {
    public static final Position ZERO = definedBy(0, 0, 0);

    private final int horizontal;
    private final int depth;
    private final int vertical;

    public Position shift(int xShift, int yShift, int zShift) {
        return definedBy(horizontal + xShift, depth + yShift, vertical + zShift);
    }
}
