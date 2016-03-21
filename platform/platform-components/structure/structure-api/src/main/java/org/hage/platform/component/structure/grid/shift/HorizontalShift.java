package org.hage.platform.component.structure.grid.shift;

import org.hage.platform.component.structure.definition.Position;

public enum HorizontalShift implements PositionShiftPredicate {
    RIGHT {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getHorizontal() > originalPosition.getHorizontal();
        }
    },
    LEFT {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getHorizontal() < originalPosition.getHorizontal();
        }
    },
}
