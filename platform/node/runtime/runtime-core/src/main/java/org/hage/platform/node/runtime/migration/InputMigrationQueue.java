package org.hage.platform.node.runtime.migration;

import org.hage.platform.node.runtime.migration.internal.InternalMigrationGroup;

import java.util.Collection;

public interface InputMigrationQueue {
    void acceptMigrants(Collection<InternalMigrationGroup> migrationGroups);
}
