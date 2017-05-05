package org.hage.platform.node.runtime.migration.external;

public interface ExternalMigrationTaskFactory {
    ExternalMigrationPerformingTask taskForGroup(ExternalMigrationGroup migrationGroup);
}
