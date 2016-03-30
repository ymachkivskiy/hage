package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.*;
import org.hage.platform.component.structure.connections.grid.calc.RelativeNeighborsCalculator;

import static org.hage.platform.component.structure.connections.Position.ZERO;
import static org.hage.platform.component.structure.connections.grid.GridNeighborhoodType.NO_NEIGHBORS;
import static org.hage.platform.component.structure.connections.grid.NeighborhoodBuilder.neighborhoodBuilder;

class FullGridStructure implements Structure {

    private final RelativeNeighborsCalculator relativeNeighborsCalculator;
    private final Chunk baseChunk;
    private final GridNeighborhoodType neighborhoodType;


    public FullGridStructure(GridNeighborhoodType neighborhoodType, Dimensions dimensions, BoundaryConditions boundaryConditions) {
        this.baseChunk = new Chunk(ZERO, dimensions);
        this.relativeNeighborsCalculator = new RelativeNeighborsCalculator(boundaryConditions, dimensions, neighborhoodType);
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
    public Neighborhood getNeighborhoodOf(Position position) {
        if (!belongsToStructure(position) || neighborhoodType == NO_NEIGHBORS) {
            return EmptyNeighborhood.INSTANCE;
        }
        return calculateFor(position);
    }


    public Neighborhood calculateFor(Position position) {
        NeighborhoodBuilder neighborhoodBuilder = neighborhoodBuilder();

        for (RelativePosition relativePosition : RelativePosition.values()) {
            neighborhoodBuilder.withNeighborsFor(relativePosition, relativeNeighborsCalculator.neighborsOf(position, relativePosition));
        }

        return neighborhoodBuilder.build();
    }

}
