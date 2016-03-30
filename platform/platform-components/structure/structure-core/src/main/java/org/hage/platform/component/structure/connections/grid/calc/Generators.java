package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.grid.BoundaryConditions;
import org.hage.platform.component.structure.connections.grid.Dimensions;
import org.hage.platform.component.structure.connections.grid.GridNeighborhoodType;
import org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.ShiftVector;
import org.hage.platform.component.structure.connections.util.AxisPerpendicularity;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.ShiftVector.newZeroShift;
import static org.hage.platform.component.structure.connections.util.AxisCoordinateUtil.prepareShiftForAxisCoordinate;

class Generators {
    private static final EnumMap<GridNeighborhoodType, Function<PositionBoundsChecker, NeighborsGenerator>> generatorsMap = new EnumMap<>(GridNeighborhoodType.class);

    static {
        generatorsMap.put(GridNeighborhoodType.FIRST_DEGREE, FirstDegreeNeighborhoodGenerator::new);
        generatorsMap.put(GridNeighborhoodType.SECOND_DEGREE, SecondDegreeNeighborhoodGenerator::new);
        generatorsMap.put(GridNeighborhoodType.THIRD_DEGREE, ThirdDegreeNeighborhoodGenerator::new);
    }


    public static NeighborsGenerator neighborsGeneratorFor(GridNeighborhoodType neighborhoodType, BoundaryConditions boundaryConditions, Dimensions gridDimensions) {
        return ofNullable(generatorsMap.get(neighborhoodType))
            .orElseThrow(() -> new StructureException("Neighbors generator not available for " + neighborhoodType + " neighborhood type."))
            .apply(new PositionBoundsChecker(boundaryConditions, gridDimensions));
    }


    private static class FirstDegreeNeighborhoodGenerator extends NeighborsGenerator {
        protected static final int shifts[] = {-1, 1};

        public FirstDegreeNeighborhoodGenerator(PositionBoundsChecker positionBoundsChecker) {
            super(positionBoundsChecker);
        }

        @Override
        protected List<Position> getEnrichedPositions(Position nearestNeighbor, AxisPerpendicularity.Plane perpendicularPlane) {
            return singletonList(nearestNeighbor);
        }
    }

    private static class SecondDegreeNeighborhoodGenerator extends FirstDegreeNeighborhoodGenerator {

        public SecondDegreeNeighborhoodGenerator(PositionBoundsChecker positionBoundsChecker) {
            super(positionBoundsChecker);
        }

        @Override
        protected List<Position> getEnrichedPositions(Position nearestNeighbor, AxisPerpendicularity.Plane perpendicularPlane) {
            List<Position> neighbors = new LinkedList<>(super.getEnrichedPositions(nearestNeighbor, perpendicularPlane));

            for (int shift : shifts) {

                ShiftVector shiftVector = newZeroShift();
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.firstAxis, shift);

                Position shiftedPosition = shiftVector.shiftPosition(nearestNeighbor);

                if (positionBoundsChecker.isLegal(shiftedPosition)) {
                    neighbors.add(shiftedPosition);
                }


                shiftVector = newZeroShift();
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.secondAxis, shift);

                shiftedPosition = shiftVector.shiftPosition(nearestNeighbor);

                if (positionBoundsChecker.isLegal(shiftedPosition)) {
                    neighbors.add(shiftedPosition);
                }

            }

            return neighbors;
        }
    }

    private static class ThirdDegreeNeighborhoodGenerator extends SecondDegreeNeighborhoodGenerator {

        public ThirdDegreeNeighborhoodGenerator(PositionBoundsChecker positionBoundsChecker) {
            super(positionBoundsChecker);
        }


        @Override
        protected List<Position> getEnrichedPositions(Position nearestNeighbor, AxisPerpendicularity.Plane perpendicularPlane) {
            List<Position> neighbors = new LinkedList<>(super.getEnrichedPositions(nearestNeighbor, perpendicularPlane));

            for (int shift : shifts) {

                ShiftVector shiftVector = newZeroShift();
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.firstAxis, shift);
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.secondAxis, shift);

                Position shiftedPosition = shiftVector.shiftPosition(nearestNeighbor);

                if (positionBoundsChecker.isLegal(shiftedPosition)) {
                    neighbors.add(shiftedPosition);
                }


                shiftVector = newZeroShift();
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.firstAxis, -shift);
                prepareShiftForAxisCoordinate(shiftVector, perpendicularPlane.secondAxis, shift);

                shiftedPosition = shiftVector.shiftPosition(nearestNeighbor);

                if (positionBoundsChecker.isLegal(shiftedPosition)) {
                    neighbors.add(shiftedPosition);
                }

            }

            return neighbors;
        }
    }

}
