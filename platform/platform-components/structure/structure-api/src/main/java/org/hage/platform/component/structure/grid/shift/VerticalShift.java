package org.hage.platform.component.structure.grid.shift;

import org.hage.platform.component.structure.definition.Position;

public enum VerticalShift implements PositionShiftPredicate {
    ABOVE {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getHorizontal() > originalPosition.getHorizontal();
        }
    },
    BELOW {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getHorizontal() < originalPosition.getHorizontal();
        }
    }
}
