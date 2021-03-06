package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.*;
import org.hage.platform.component.structure.connections.grid.calc.RelativeNeighborsCalculator;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.structure.grid.Dimensions;
import org.hage.platform.component.structure.grid.GridNeighborhoodType;

import static org.hage.platform.component.structure.Position.ZERO;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.NO_NEIGHBORS;
import static org.hage.platform.component.structure.connections.grid.NeighborhoodBuilder.neighborhoodBuilder;

class FullGridStructure implements Structure {

    private final RelativeNeighborsCalculator relativeNeighborsCalculator;
    private final Chunk baseChunk;
    private final GridNeighborhoodType neighborhoodType;


    public FullGridStructure(GridNeighborhoodType neighborhoodType, Dimensions dimensions, GridBoundaryConditions gridBoundaryConditions) {
        this.baseChunk = new Chunk(ZERO, dimensions);
        this.relativeNeighborsCalculator = new RelativeNeighborsCalculator(gridBoundaryConditions, dimensions, neighborhoodType);
        this.neighborhoodType = neighborhoodType;
    }

    @Override
    public boolean belongsToStructure(Position position) {
        return baseChunk.containsPosition(position);
    }

    @Override
    public boolean areNeighbors(Position first, Position second) {
        return belongsToStructure(first)
            && belongsToStructure(second)
            && getNeighborhoodOf(first).getAllNeighbors().contains(second);
    }

    @Override
    public StructuralNeighborhood getNeighborhoodOf(Position position) {
        if (!belongsToStructure(position) || neighborhoodType == NO_NEIGHBORS) {
            return EmptyStructuralNeighborhood.INSTANCE;
        }
        return calculateFor(position);
    }


    public StructuralNeighborhood calculateFor(Position position) {
        NeighborhoodBuilder neighborhoodBuilder = neighborhoodBuilder();

        for (RelativePosition relativePosition : RelativePosition.values()) {
            neighborhoodBuilder.withNeighborsFor(relativePosition, relativeNeighborsCalculator.neighborsOf(position, relativePosition));
        }

        return neighborhoodBuilder.build();
    }

}
