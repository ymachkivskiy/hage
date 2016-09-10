package org.hage.platform.component.runtime.migration;

import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;

import java.util.Collection;

public interface InputMigrationQueue {
    void acceptMigrants(Collection<InternalMigrationGroup> migrationGroups);
}
