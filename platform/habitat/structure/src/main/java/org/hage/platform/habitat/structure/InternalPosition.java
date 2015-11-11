package org.hage.platform.habitat.structure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

@Immutable
@EqualsAndHashCode(doNotUseGetters = true)
@Data(staticConstructor = "definedBy")
public final class InternalPosition {
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
