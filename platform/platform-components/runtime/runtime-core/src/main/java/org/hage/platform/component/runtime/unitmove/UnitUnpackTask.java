package org.hage.platform.component.runtime.unitmove;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.runtime.migration.InputMigrationQueue;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.singletonList;

@PrototypeComponent
@RequiredArgsConstructor
@Slf4j
public class UnitUnpackTask implements Runnable {

    private final PackedUnit packedUnit;

    @Autowired
    private UnitConfigurationActivator unitConfigurationActivator;
    @Autowired
    private InputMigrationQueue inputMigrationQueue;

    @Override
    public void run() {
        log.debug("Unpacking unit {}", packedUnit);

        Position unitPosition = packedUnit.getPosition();

        unitConfigurationActivator.activateConfigurationOnPosition(unitPosition, packedUnit.getConfiguration());
        inputMigrationQueue.acceptMigrants(singletonList(new InternalMigrationGroup(unitPosition, packedUnit.getAgents())));
    }

}
