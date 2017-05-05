package org.hage.platform.node.runtime.migration.remote;

import org.hage.platform.node.runtime.migration.external.ExternalMigrationGroup;

public interface ExternalMigrationSender {
    void sendMigrants(ExternalMigrationGroup migrationGroup);
}
