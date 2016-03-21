package org.hage.platform.component.structure.grid.shift;

import org.hage.platform.component.structure.definition.Position;

public interface PositionShiftPredicate {
    boolean isMatching(Position originalPosition, Position shiftedPosition);
}
