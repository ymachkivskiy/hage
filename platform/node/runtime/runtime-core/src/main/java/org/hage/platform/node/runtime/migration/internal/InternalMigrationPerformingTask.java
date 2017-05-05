package org.hage.platform.node.runtime.migration.internal;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.node.runtime.unit.AgentMigrationTarget;
import org.hage.platform.node.runtime.unit.registry.MigrationTargetRegistry;
import org.springframework.beans.factory.annotation.Autowired;

@PrototypeComponent
@RequiredArgsConstructor
@Slf4j
public class InternalMigrationPerformingTask implements Runnable {

    private final InternalMigrationGroup migrationGroup;

    @Autowired
    private MigrationTargetRegistry migrationTargetRegistry;

    @Override
    public void run() {
        log.debug("Hosting migration group {}", migrationGroup);

        AgentMigrationTarget migrationTarget = migrationTargetRegistry.migrationTargetFor(migrationGroup.getTargetPosition());
        migrationTarget.addAgentsImmediately(migrationGroup.getMigrants());
    }

}
