package org.hage.platform.component.structure.grid.shift;

import org.hage.platform.component.structure.definition.Position;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

public class PositionPredicate implements Predicate<Position> {

    private final Position originalPosition;
    private final List<PositionShiftPredicate> shiftPredicates;

    public PositionPredicate(Position originalPosition, PositionShiftPredicate... shiftPredicates) {
        this.originalPosition = originalPosition;
        this.shiftPredicates = asList(shiftPredicates);
    }

    @Override
    public boolean test(Position position) {

        for (PositionShiftPredicate shiftPredicate : shiftPredicates) {
            if (!shiftPredicate.isMatching(originalPosition, position)) {
                return false;
            }
        }

        return true;
    }

}
