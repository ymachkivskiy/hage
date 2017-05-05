package org.hage.platform.node.runtime.unitmove;

import org.hage.platform.node.structure.Position;

public interface UnitDeactivationPacker {
    PackedUnit deactivateAndPack(Position position);
}
