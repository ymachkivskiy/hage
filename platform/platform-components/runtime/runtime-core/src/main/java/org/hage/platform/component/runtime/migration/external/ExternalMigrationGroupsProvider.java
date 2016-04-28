package org.hage.platform.component.runtime.migration.external;

import java.util.List;

public interface ExternalMigrationGroupsProvider {
    List<ExternalMigrationGroup> takeExternalMigrationGroups();
}
