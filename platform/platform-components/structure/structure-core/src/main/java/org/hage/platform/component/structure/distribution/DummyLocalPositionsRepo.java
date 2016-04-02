package org.hage.platform.component.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.Position;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

@Slf4j
public class DummyLocalPositionsRepo implements LocalPositionsController, AddressingRegistry {

    @Override
    public PositionAddress queryPositionAddress(Position position) {
        return new PositionAddress(PositionState.NOT_CORRECT, null);
    }

    @Override
    public void activate(Collection<Position> positions) {
        // TODO: implement
    }

    @Override
    public void activate(Position position) {
        activate(singletonList(position));
    }

    @Override
    public void deactivate(List<Position> positions) {

    }
}
