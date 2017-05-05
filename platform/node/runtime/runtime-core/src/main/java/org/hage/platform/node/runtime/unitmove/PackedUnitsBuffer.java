package org.hage.platform.node.runtime.unitmove;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@SingletonComponent
class PackedUnitsBuffer implements UnitUnpackingQueue, PackedUnitsProvider {

    private final List<PackedUnit> innerBuffer = new LinkedList<>();

    @Override
    public synchronized Collection<PackedUnit> takePackedUnits() {

        List<PackedUnit> packedUnits = new ArrayList<>(innerBuffer);
        innerBuffer.clear();

        log.debug("Take packed units : {}", packedUnits);

        return packedUnits;
    }

    @Override
    public synchronized void scheduleUnpackAndActivation(Collection<PackedUnit> packedUnits) {
        log.debug("Schedule unpack and activation of {}", packedUnits);

        innerBuffer.addAll(packedUnits);
    }

}
