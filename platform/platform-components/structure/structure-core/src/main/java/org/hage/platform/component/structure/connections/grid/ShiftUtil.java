package org.hage.platform.component.structure.connections.grid;

public class ShiftUtil {

//    private final static EnumMap<DepthShift, DepthShifter> depthShifters;
//    private final static EnumMap<VerticalShift, VerticalShifter> verticalShifters;
//    private final static EnumMap<HorizontalShift, HorizontalShifter> horizontalShifters;
//
//    static {
//        depthShifters = new EnumMap<>(of(
//            AT_BACK, new DepthShifter(AT_BACK),
//            IN_FRONT, new DepthShifter(IN_FRONT)
//        ));
//        verticalShifters = new EnumMap<>(of(
//            ABOVE, new VerticalShifter(ABOVE),
//            BELOW, new VerticalShifter(BELOW)
//        ));
//        horizontalShifters = new EnumMap<>(of(
//            ON_LEFT, new HorizontalShifter(ON_LEFT),
//            ON_RIGHT, new HorizontalShifter(ON_RIGHT)
//        ));
//    }
//
//
//    public static Shifter shifterFor(HorizontalShift horizontalShift) {
//        return ofNullable(horizontalShifters.get(horizontalShift))
//            .orElseThrow(() -> new StructureException("No shifter registered for " + horizontalShift + " horizontal shift"));
//    }
//
//    public static Shifter shifterFor(DepthShift depthShift) {
//        return ofNullable(depthShifters.get(depthShift))
//            .orElseThrow(() -> new StructureException("No shifter registered for " + depthShift + " depth shift"));
//    }
//
//    public static Shifter shifterFor(VerticalShift verticalShift) {
//        return ofNullable(verticalShifters.get(verticalShift))
//            .orElseThrow(() -> new StructureException("No shifter registered for " + verticalShift + " vertical shift"));
//    }
//
//    private static class DepthShifter extends Shifter {
//
//        public DepthShifter(DepthShift depthShift) {
//            super(DEPTH, depthShift == IN_FRONT ? 1 : -1);
//        }
//
//        @Override
//        public Position shift(Position originalPosition) {
//            return originalPosition.shift(direction, 0, 0);
//        }
//
//        @Override
//        public Position shiftOnBound(Position originalPosition, Dimensions dimensions) {
//            return direction == 1
//                ? originalPosition.withDepth(0)
//                : originalPosition.withDepth(dimensions.depthSize - 1)
//                ;
//        }
//    }
//
//    private static class HorizontalShifter extends Shifter {
//
//
//        public HorizontalShifter(HorizontalShift horizontalShift) {
//            super(HORIZONTAL, horizontalShift == ON_RIGHT ? 1 : -1);
//        }
//
//        @Override
//        public Position shift(Position originalPosition) {
//            return originalPosition.shift(0, direction, 0);
//        }
//
//        @Override
//        public Position shiftOnBound(Position originalPosition, Dimensions dimensions) {
//            return direction == 1
//                ? originalPosition.withHorizontal(0)
//                : originalPosition.withHorizontal(dimensions.horizontalSize - 1)
//                ;
//        }
//    }
//
//    private static class VerticalShifter extends Shifter {
//
//        public VerticalShifter(VerticalShift verticalShift) {
//            super(VERTICAL, verticalShift == ABOVE ? 1 : -1);
//        }
//
//        @Override
//        public Position shift(Position originalPosition) {
//            return originalPosition.shift(0, 0, direction);
//        }
//
//        @Override
//        public Position shiftOnBound(Position originalPosition, Dimensions dimensions) {
//            return direction == 1
//                ? originalPosition.withVertical(0)
//                : originalPosition.withVertical(dimensions.verticalSize - 1)
//                ;
//        }
//    }
//
//
//    @RequiredArgsConstructor
//    public static abstract class Shifter {
//
//        @Getter
//        private final Axis axis;
//        protected final int direction;
//
//        public abstract Position shift(Position originalPosition);
//
//        public abstract Position shiftOnBound(Position originalPosition, Dimensions dimensions);
//    }

}
