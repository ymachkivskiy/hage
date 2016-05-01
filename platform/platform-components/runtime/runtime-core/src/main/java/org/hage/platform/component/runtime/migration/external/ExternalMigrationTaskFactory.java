package org.hage.platform.component.runtime.migration.external;

public interface ExternalMigrationTaskFactory {
    ExternalMigrationPerformingTask taskForGroup(ExternalMigrationGroup migrationGroup);
}
