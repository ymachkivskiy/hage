package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.Dimensions;

import java.util.EnumSet;

import static java.util.EnumSet.noneOf;
import static org.hage.platform.component.structure.Axis.*;
import static org.hage.platform.component.structure.connections.grid.config.BoundaryAxisMirroringConfig.axisesForbiddenForMirroring;

class PositionBoundsChecker {
    private final Dimensions dimensions;
    private final EnumSet<Axis> axisesWithForbiddenMirroring;

    public PositionBoundsChecker(GridBoundaryConditions gridBoundaryConditions, Dimensions dimensions) {
        this.dimensions = dimensions;
        this.axisesWithForbiddenMirroring = axisesForbiddenForMirroring(gridBoundaryConditions);
    }

    public boolean isLegal(Position position) {
        EnumSet<Axis> forbidenExceededAxises = getAxisesWithExceededBounds(position);
        forbidenExceededAxises.retainAll(axisesWithForbiddenMirroring);
        return forbidenExceededAxises.isEmpty();
    }

    private EnumSet<Axis> getAxisesWithExceededBounds(Position position) {
        EnumSet<Axis> axises = noneOf(Axis.class);

        if (position.depth < 0 || position.depth >= dimensions.depthSize) {
            axises.add(DEPTH);
        }

        if (position.horizontal < 0 || position.horizontal >= dimensions.horizontalSize) {
            axises.add(HORIZONTAL);
        }

        if (position.vertical < 0 || position.vertical >= dimensions.verticalSize) {
            axises.add(VERTICAL);
        }

        return axises;
    }

}
