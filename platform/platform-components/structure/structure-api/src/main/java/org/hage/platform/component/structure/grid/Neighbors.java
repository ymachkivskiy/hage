package org.hage.platform.component.structure.grid;

import org.hage.platform.component.structure.definition.Position;
import org.hage.platform.component.structure.grid.shift.DepthShift;
import org.hage.platform.component.structure.grid.shift.HorizontalShift;
import org.hage.platform.component.structure.grid.shift.PositionPredicate;
import org.hage.platform.component.structure.grid.shift.VerticalShift;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class Neighbors {

    private final Position centralPosition;
    private final List<Position> positions;

    public Neighbors(Position centralPosition, List<Position> positions) {
        this.centralPosition = centralPosition;
        this.positions = unmodifiableList(positions != null && !positions.isEmpty() ? new ArrayList<>(positions) : emptyList());
    }

    public boolean arePresent() {
        return positions != null && !positions.isEmpty();
    }

    public List<Position> all() {
        return positions;
    }

    public List<Position> choose(DepthShift depthShift) {
        return matching(new PositionPredicate(centralPosition, depthShift));
    }

    public List<Position> choose(HorizontalShift horizontalShift) {
        return matching(new PositionPredicate(centralPosition, horizontalShift));
    }

    public List<Position> choose(VerticalShift verticalShift) {
        return matching(new PositionPredicate(centralPosition, verticalShift));
    }

    public List<Position> choose(DepthShift depthShift, HorizontalShift horizontalShift) {
        return matching(new PositionPredicate(centralPosition, depthShift, horizontalShift));
    }

    public List<Position> choose(DepthShift depthShift, VerticalShift horizontal) {
        return matching(new PositionPredicate(centralPosition, depthShift, horizontal));
    }

    public List<Position> choose(HorizontalShift horizontalShift, VerticalShift verticalShift) {
        return matching(new PositionPredicate(centralPosition, horizontalShift, verticalShift));
    }

    public List<Position> choose(DepthShift depthShift, HorizontalShift horizontalShift, VerticalShift verticalShift) {
        return matching(new PositionPredicate(centralPosition, depthShift, horizontalShift, verticalShift));
    }

    public List<Position> matching(Predicate<Position> predicate) {
        return positions.stream()
            .filter(predicate)
            .collect(toList());
    }


}
