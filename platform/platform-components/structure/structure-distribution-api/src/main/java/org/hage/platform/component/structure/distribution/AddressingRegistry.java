package org.hage.platform.component.structure.distribution;

import org.hage.platform.component.structure.Position;

public interface AddressingRegistry {
    PositionAddress queryPositionAddress(Position position);
}