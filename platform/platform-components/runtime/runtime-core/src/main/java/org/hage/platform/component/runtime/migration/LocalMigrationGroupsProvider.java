package org.hage.platform.component.runtime.migration;

import java.util.List;

public interface LocalMigrationGroupsProvider {
    List<LocalMigrationGroup> takeMigrationGroups();
}
