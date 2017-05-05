package org.hage.platform.node.structure.connections.grid.calc;

import org.hage.platform.node.structure.Axis;
import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.grid.GridBoundaryConditions;
import org.hage.platform.node.structure.grid.Dimensions;
import org.hage.platform.node.structure.connections.grid.config.BoundaryAxisMirroringConfig;

import java.util.EnumSet;

import static java.util.EnumSet.noneOf;
import static org.hage.platform.node.structure.Axis.*;

class PositionBoundsChecker {
    private final Dimensions dimensions;
    private final EnumSet<Axis> axisesWithForbiddenMirroring;

    public PositionBoundsChecker(GridBoundaryConditions gridBoundaryConditions, Dimensions dimensions) {
        this.dimensions = dimensions;
        this.axisesWithForbiddenMirroring = BoundaryAxisMirroringConfig.axisesForbiddenForMirroring(gridBoundaryConditions);
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
