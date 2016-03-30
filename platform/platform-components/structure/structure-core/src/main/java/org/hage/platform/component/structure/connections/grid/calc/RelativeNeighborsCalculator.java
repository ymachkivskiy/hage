package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.connections.grid.BoundaryConditions;
import org.hage.platform.component.structure.connections.grid.Dimensions;
import org.hage.platform.component.structure.connections.grid.GridNeighborhoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hage.platform.component.structure.connections.grid.calc.Generators.neighborsGeneratorFor;

public class RelativeNeighborsCalculator {
    private final PositionReflector reflector;
    private final NeighborsGenerator neighborsGenerator;

    public RelativeNeighborsCalculator(BoundaryConditions boundaryConditions, Dimensions gridDimensions, GridNeighborhoodType neighborhoodType) {
        this.reflector = new PositionReflector(gridDimensions);
        this.neighborsGenerator = neighborsGeneratorFor(neighborhoodType, boundaryConditions, gridDimensions);
    }

    public List<Position> neighborsOf(Position position, RelativePosition relativePosition) {
        Set<Position> positions = neighborsGenerator.generate(position, relativePosition)
            .stream()
            .map(reflector::mirrorIfRequired)
            .collect(toSet());

        return new ArrayList<>(positions);
    }

}
