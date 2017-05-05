package org.hage.platform.node.runtime.unitmove;

public interface UnitUnpackTaskFactory {
    UnitUnpackTask createTask(PackedUnit packedUnit);
}
