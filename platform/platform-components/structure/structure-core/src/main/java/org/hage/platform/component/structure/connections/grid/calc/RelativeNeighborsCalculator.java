package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.Dimensions;
import org.hage.platform.component.structure.grid.GridNeighborhoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hage.platform.component.structure.connections.grid.calc.Generators.neighborsGeneratorFor;

public class RelativeNeighborsCalculator {
    private final PositionReflector reflector;
    private final NeighborsGenerator neighborsGenerator;

    public RelativeNeighborsCalculator(GridBoundaryConditions gridBoundaryConditions, Dimensions gridDimensions, GridNeighborhoodType neighborhoodType) {
        this.reflector = new PositionReflector(gridDimensions);
        this.neighborsGenerator = neighborsGeneratorFor(neighborhoodType, gridBoundaryConditions, gridDimensions);
    }

    public List<Position> neighborsOf(Position position, RelativePosition relativePosition) {
        Set<Position> positions = neighborsGenerator.generate(position, relativePosition)
            .stream()
            .map(reflector::mirrorIfRequired)
            .collect(toSet());

        return new ArrayList<>(positions);
    }

}
