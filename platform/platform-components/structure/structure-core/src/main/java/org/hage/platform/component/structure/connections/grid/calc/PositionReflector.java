package org.hage.platform.component.structure.connections.grid.calc;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.grid.Dimensions;

import static java.lang.Math.floorMod;
import static org.hage.platform.component.structure.connections.Position.position;

@RequiredArgsConstructor
class PositionReflector {
    private final Dimensions dimensions;

    public Position mirrorIfRequired(Position originalPosition) {
        return position(
            floorMod(originalPosition.depth, dimensions.depthSize),
            floorMod(originalPosition.horizontal, dimensions.horizontalSize),
            floorMod(originalPosition.vertical, dimensions.verticalSize));
    }

}
