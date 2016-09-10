package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.ShiftVector;
import org.hage.platform.component.structure.connections.util.AxisPerpendicularity.Plane;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.Dimensions;
import org.hage.platform.component.structure.grid.GridNeighborhoodType;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.ShiftVector.newZeroShift;
import static org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.updateShiftVectorForAxis;

class Generators {
    private static final EnumMap<GridNeighborhoodType, Function<PositionBoundsChecker, NeighborsGenerator>> generatorsMap = new EnumMap<>(GridNeighborhoodType.class);

    static {
        generatorsMap.put(GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD, VonNeumanNeighborhood::new);
        generatorsMap.put(GridNeighborhoodType.MOORE_NEIGHBORHOOD, MooreNeighborhood::new);
    }


    public static NeighborsGenerator neighborsGeneratorFor(GridNeighborhoodType neighborhoodType, GridBoundaryConditions gridBoundaryConditions, Dimensions gridDimensions) {
        return ofNullable(generatorsMap.get(neighborhoodType))
            .orElseThrow(() -> new StructureException("Neighbors generator not available for " + neighborhoodType + " neighborhood type."))
            .apply(new PositionBoundsChecker(gridBoundaryConditions, gridDimensions));
    }


    private static class VonNeumanNeighborhood extends NeighborsGenerator {
        protected static final int shifts[] = {-1, 1};

        public VonNeumanNeighborhood(PositionBoundsChecker positionBoundsChecker) {
            super(positionBoundsChecker);
        }

        @Override
        protected List<Position> getEnrichedPositions(Position nearestNeighbor, Plane perpendicularPlane) {
            return singletonList(nearestNeighbor);
        }
    }

    private static class MooreNeighborhood extends VonNeumanNeighborhood {

        public MooreNeighborhood(PositionBoundsChecker positionBoundsChecker) {
            super(positionBoundsChecker);
        }


        @Override
        protected List<Position> getEnrichedPositions(Position nearestNeighbor, Plane perpendicularPlane) {
            List<Position> neighbors = new LinkedList<>(super.getEnrichedPositions(nearestNeighbor, perpendicularPlane));

            final Axis fAxis = perpendicularPlane.firstAxis;
            final Axis sAxis = perpendicularPlane.secondAxis;

            for (int shift : shifts) {
                ShiftVector shiftV;

                shiftV = newZeroShift();
                updateShiftVectorForAxis(shiftV, fAxis, shift);
                appendCorrectNeighbor(nearestNeighbor, shiftV, neighbors);

                updateShiftVectorForAxis(shiftV, sAxis, shift);
                appendCorrectNeighbor(nearestNeighbor, shiftV, neighbors);

                shiftV = newZeroShift();
                updateShiftVectorForAxis(shiftV, sAxis, shift);
                appendCorrectNeighbor(nearestNeighbor, shiftV, neighbors);

                updateShiftVectorForAxis(shiftV, fAxis, -shift);
                appendCorrectNeighbor(nearestNeighbor, shiftV, neighbors);
            }

            return neighbors;
        }

        private void appendCorrectNeighbor(Position central, ShiftVector shiftV, List<Position> neighbors) {
            Position neighbor = shiftV.shiftPosition(central);
            if (positionBoundsChecker.isLegal(neighbor)) {
                neighbors.add(neighbor);
            }
        }
    }

}
