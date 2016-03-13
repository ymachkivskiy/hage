package org.hage.platform.component.simulation.structure.definition;

import lombok.Data;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@Data(staticConstructor = "definedBy")
public final class InternalPosition implements Serializable {
    public static final InternalPosition ZERO = definedBy(0, 0, 0);

    private final int xPos;
    private final int yPos;
    private final int zPos;

    public InternalPosition shift(int xShift, int yShift, int zShift) {
        return definedBy(xPos + xShift, yPos + yShift, zPos + zShift);
    }
}
