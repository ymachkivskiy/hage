package org.hage.platform.habitat.structure;

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

    public InternalPosition withPosX(int newX) {
        return definedBy(newX, yPos, zPos);
    }

    public InternalPosition withPosY(int newY) {
        return definedBy(xPos, newY, zPos);
    }

    public InternalPosition withPosZ(int newZ) {
        return definedBy(xPos, yPos, newZ);
    }

    public InternalPosition shift(int xShift, int yShift, int zShift) {
        return definedBy(xPos + xShift, yPos + yShift, zPos + zShift);
    }
}
