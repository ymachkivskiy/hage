package org.hage.platform.component.runtime.migration.internal;

public interface InternalMigrationTaskFactory {
    InternalMigrationPerformingTask migrationTaskFor(InternalMigrationGroup internalMigrationGroup);
}
