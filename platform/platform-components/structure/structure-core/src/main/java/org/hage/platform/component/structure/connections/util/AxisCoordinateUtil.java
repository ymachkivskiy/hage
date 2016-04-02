package org.hage.platform.component.structure.connections.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.Position;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.structure.Axis.*;

public class AxisCoordinateUtil {

    private static final Map<Axis, CoordinateUtil> coordinateExtractorsMap = new EnumMap<>(Axis.class);

    static {
        coordinateExtractorsMap.put(
            HORIZONTAL,
            new CoordinateUtil(
                position -> position.horizontal,
                ShiftVector::setHorizontalShift
            )
        );

        coordinateExtractorsMap.put(
            VERTICAL,
            new CoordinateUtil(
                position -> position.vertical,
                ShiftVector::setVerticalShift
            )
        );

        coordinateExtractorsMap.put(
            DEPTH,
            new CoordinateUtil(
                position -> position.depth,
                ShiftVector::setDepthShift
            )
        );
    }

    public static int axisCoordinateOf(Position position, Axis axis) {
        return coordinateExtractorsMap.get(axis).coordinateExtractor.apply(position);
    }

    public static void prepareShiftForAxisCoordinate(ShiftVector shiftVector, Axis axis, int shiftDelta) {
        coordinateExtractorsMap.get(axis).shiftUpdater.accept(shiftVector, shiftDelta);
    }


    @Data
    private static class CoordinateUtil {
        private final Function<Position, Integer> coordinateExtractor;
        private final BiConsumer<ShiftVector, Integer> shiftUpdater;
    }

    @AllArgsConstructor(access = PRIVATE)
    @Getter
    @Setter
    public static class ShiftVector {
        private int depthShift;
        private int horizontalShift;
        private int verticalShift;

        public static ShiftVector newZeroShift() {
            return new ShiftVector(0, 0, 0);
        }

        public Position shiftPosition(Position position) {
            return position.shift(depthShift, horizontalShift, verticalShift);
        }
    }
}
