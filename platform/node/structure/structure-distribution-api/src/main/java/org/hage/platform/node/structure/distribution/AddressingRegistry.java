package org.hage.platform.node.structure.distribution;

import org.hage.platform.node.structure.Position;

import java.util.List;

public interface AddressingRegistry {
    PositionAddressState queryPositionAddressState(Position position);

    List<ActivePositionAddress> queryActivePositionAddresses();

    List<Position> queryLocalActivePositions();
}