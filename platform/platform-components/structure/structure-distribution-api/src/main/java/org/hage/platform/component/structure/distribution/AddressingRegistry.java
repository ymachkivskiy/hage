package org.hage.platform.component.structure.distribution;

import org.hage.platform.component.structure.Position;

import java.util.List;

public interface AddressingRegistry {
    PositionAddressState queryPositionAddressState(Position position);

    List<ActivePositionAddress> queryActivePositionAddresses();

    List<Position> queryLocalActivePositions();
}