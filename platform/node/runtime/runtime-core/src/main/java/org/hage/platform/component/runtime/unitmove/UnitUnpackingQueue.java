package org.hage.platform.component.runtime.unitmove;

import java.util.Collection;

public interface UnitUnpackingQueue {
    void scheduleUnpackAndActivation(Collection<PackedUnit> packedUnits);
}
