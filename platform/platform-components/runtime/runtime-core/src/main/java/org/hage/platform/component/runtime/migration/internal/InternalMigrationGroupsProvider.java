package org.hage.platform.component.runtime.migration.internal;

import java.util.List;

public interface InternalMigrationGroupsProvider {
    List<InternalMigrationGroup> takeMigrationGroups();
}
