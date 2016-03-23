package org.hage.platform.component.structure.connections.grid;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.connections.Position;

import java.util.EnumSet;
import java.util.Set;

import static java.util.EnumSet.noneOf;
import static org.hage.platform.component.structure.Axis.*;

@RequiredArgsConstructor
public class GridBoundaryPositionsChecker {

    private final Dimensions dimensions;

    public Set<Axis> getBoundaryAxises(Position position) {
        EnumSet<Axis> result = noneOf(Axis.class);

        if (position.depth == 0 || position.depth == dimensions.depthSize - 1) {
            result.add(DEPTH);
        }

        if (position.vertical == 0 || position.vertical == dimensions.verticalSize - 1) {
            result.add(VERTICAL);
        }

        if (position.horizontal == 0 || position.horizontal == dimensions.horizontalSize - 1) {
            result.add(HORIZONTAL);
        }

        return result;
    }

}
