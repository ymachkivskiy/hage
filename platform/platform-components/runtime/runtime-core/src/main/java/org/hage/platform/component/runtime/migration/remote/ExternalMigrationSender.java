package org.hage.platform.component.runtime.migration.remote;

import org.hage.platform.component.runtime.migration.external.ExternalMigrationGroup;

public interface ExternalMigrationSender {
    void sendMigrants(ExternalMigrationGroup migrationGroup);
}
