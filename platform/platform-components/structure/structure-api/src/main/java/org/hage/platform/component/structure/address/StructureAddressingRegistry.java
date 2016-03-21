package org.hage.platform.component.structure.address;

import org.hage.platform.component.structure.definition.Position;

public interface StructureAddressingRegistry {
    PositionAddressingInfo getCurrentAddressingInfo(Position position);
}