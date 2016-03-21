package org.hage.platform.component.structure.grid.shift;

import org.hage.platform.component.structure.definition.Position;

public enum DepthShift implements PositionShiftPredicate {
    FRONT {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getDepth() > originalPosition.getDepth();
        }
    },
    BACK {
        @Override
        public boolean isMatching(Position originalPosition, Position shiftedPosition) {
            return shiftedPosition.getDepth() < originalPosition.getDepth();
        }
    }
}
