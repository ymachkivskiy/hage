package org.hage.platform.node.structure.connections.grid.calc;

import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.connections.RelativePosition;

import java.util.EnumMap;
import java.util.function.Function;

class PositionRelativeShifter {

    private static EnumMap<RelativePosition, Function<Position, Position>> positionShifters = new EnumMap<>(RelativePosition.class);

    static {
        positionShifters.put(RelativePosition.ABOVE, p -> p.shift(0, 0, 1));
        positionShifters.put(RelativePosition.BELOW, p -> p.shift(0, 0, -1));
        positionShifters.put(RelativePosition.ON_RIGHT, p -> p.shift(0, 1, 0));
        positionShifters.put(RelativePosition.ON_LEFT, p -> p.shift(0, -1, 0));
        positionShifters.put(RelativePosition.IN_FRONT, p -> p.shift(1, 0, 0));
        positionShifters.put(RelativePosition.AT_BACK, p -> p.shift(-1, 0, 0));
    }

    public static Position performShift(Position position, RelativePosition relativePosition) {
        return positionShifters.get(relativePosition).apply(position);
    }
}
