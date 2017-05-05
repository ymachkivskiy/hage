package org.hage.platform.node.runtime.migration.internal;

public interface InternalMigrationTaskFactory {
    InternalMigrationPerformingTask migrationTaskFor(InternalMigrationGroup internalMigrationGroup);
}
