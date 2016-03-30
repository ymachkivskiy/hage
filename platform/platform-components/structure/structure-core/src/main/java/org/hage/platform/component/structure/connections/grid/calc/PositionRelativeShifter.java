package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.RelativePosition;

import java.util.EnumMap;
import java.util.function.Function;

import static org.hage.platform.component.structure.connections.RelativePosition.*;

class PositionRelativeShifter {

    private static EnumMap<RelativePosition, Function<Position, Position>> positionShifters = new EnumMap<>(RelativePosition.class);

    static {
        positionShifters.put(ABOVE, p -> p.shift(0, 0, 1));
        positionShifters.put(BELOW, p -> p.shift(0, 0, -1));
        positionShifters.put(ON_RIGHT, p -> p.shift(0, 1, 0));
        positionShifters.put(ON_LEFT, p -> p.shift(0, -1, 0));
        positionShifters.put(IN_FRONT, p -> p.shift(1, 0, 0));
        positionShifters.put(AT_BACK, p -> p.shift(-1, 0, 0));
    }

    public static Position performShift(Position position, RelativePosition relativePosition) {
        return positionShifters.get(relativePosition).apply(position);
    }
}
