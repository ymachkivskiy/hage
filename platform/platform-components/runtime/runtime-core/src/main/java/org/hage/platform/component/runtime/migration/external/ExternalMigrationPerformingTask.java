package org.hage.platform.component.runtime.migration.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.runtime.migration.remote.ExternalMigrationSender;
import org.springframework.beans.factory.annotation.Autowired;

@PrototypeComponent
@RequiredArgsConstructor
@Slf4j
public class ExternalMigrationPerformingTask implements Runnable {

    @Autowired
    private ExternalMigrationSender externalMigrationSender;

    private final ExternalMigrationGroup migrationGroup;

    @Override
    public void run() {
        log.debug("Sending migrants {}", migrationGroup);

        externalMigrationSender.sendMigrants(migrationGroup);

        log.debug("Migrants sent.");
    }
}
