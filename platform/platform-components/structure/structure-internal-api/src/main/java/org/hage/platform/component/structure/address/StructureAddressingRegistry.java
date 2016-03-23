package org.hage.platform.component.structure.address;

import org.hage.platform.component.structure.connections.Position;

public interface StructureAddressingRegistry {
    PositionAddressingInfo getCurrentAddressingInfo(Position position);
}