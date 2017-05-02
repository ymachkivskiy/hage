package org.hage.platform.component.runtime.unitmove;

import org.hage.platform.component.structure.Position;

public interface UnitDeactivationPacker {
    PackedUnit deactivateAndPack(Position position);
}
