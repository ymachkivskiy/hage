package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.Structure;

import static org.hage.platform.component.structure.connections.Position.ZERO;

class GridStructure implements Structure {

    private final BoundaryConditions boundaryConditions;
    private final Dimensions gridDimensions;
    private final GridNeighborhoodType neighborhoodType;

    private final Chunk baseChunk;


    public GridStructure(GridNeighborhoodType neighborhoodType, Dimensions dimensions, BoundaryConditions boundaryConditions) {
        this.neighborhoodType = neighborhoodType;
        this.boundaryConditions = boundaryConditions;
        this.gridDimensions = dimensions;

        this.baseChunk = new Chunk(ZERO, dimensions);
    }

    @Override
    public boolean belongsToStructure(Position position) {
        return baseChunk.containsPosition(position);
    }

    @Override
    public boolean areNeighbors(Position first, Position second) {
//        if (belongsToStructure(first) && belongsToStructure(second)) {
//            return getNeighborsOf(first).choose(all()).contains(second);
//        }
        // TODO: implement
        return false;
    }

    @Override
    public Neighbors getNeighborsOf(Position position) {

        if (!belongsToStructure(position)) {
//            return EMPTY_NEIGHBORS;
        }
        return null;
//        return new BoxNeighbors(position);
    }

//    @RequiredArgsConstructor
//    private class BoxNeighbors implements Neighbors {
//
//        private final Position centralPosition;
//
//        @Override
//        public List<Position> choose(RelativeSelector shift) {
//            // TODO: implement
//            return null;
//        }
//    }


}
