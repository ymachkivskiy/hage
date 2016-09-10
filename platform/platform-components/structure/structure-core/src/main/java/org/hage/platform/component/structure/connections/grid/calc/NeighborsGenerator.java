package org.hage.platform.component.structure.connections.grid.calc;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.connections.util.AxisPerpendicularity.Plane;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hage.platform.component.structure.connections.grid.calc.PositionRelativeShifter.performShift;
import static org.hage.platform.component.structure.connections.util.AxisPerpendicularity.planePerpendicularTo;
import static org.hage.platform.component.structure.connections.util.RelativePositionAxis.axisOf;

@RequiredArgsConstructor
abstract class NeighborsGenerator {
    protected final PositionBoundsChecker positionBoundsChecker;

    public final List<Position> generate(Position position, RelativePosition relativePosition) {
        Position shiftedPos = performShift(position, relativePosition);

        if (positionBoundsChecker.isLegal(shiftedPos)) {
            return getEnrichedPositions(shiftedPos, planePerpendicularTo(axisOf(relativePosition)));
        } else {
            return emptyList();
        }
    }

    protected abstract List<Position> getEnrichedPositions(Position nearestNeighbor, Plane perpendicularPlane);

}
