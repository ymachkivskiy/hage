package org.hage.platform.component.runtime.unitmove;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.springframework.beans.factory.annotation.Autowired;

@PrototypeComponent
@RequiredArgsConstructor
@Slf4j
public class UnitUnpackTask implements Runnable {

    private final PackedUnit packedUnit;

    @Autowired
    private UnitConfigurationActivator unitConfigurationActivator;

    @Override
    public void run() {
        log.debug("Unpacking unit {}", packedUnit);

        unitConfigurationActivator.activateConfigurationOnPosition(packedUnit.getPosition(), packedUnit.getConfiguration());
    }

}
