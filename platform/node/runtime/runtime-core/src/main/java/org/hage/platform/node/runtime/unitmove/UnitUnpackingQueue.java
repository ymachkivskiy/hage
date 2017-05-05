package org.hage.platform.node.runtime.unitmove;

import java.util.Collection;

public interface UnitUnpackingQueue {
    void scheduleUnpackAndActivation(Collection<PackedUnit> packedUnits);
}
