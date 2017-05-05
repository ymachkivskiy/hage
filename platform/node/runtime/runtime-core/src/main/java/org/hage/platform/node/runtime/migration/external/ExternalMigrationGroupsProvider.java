package org.hage.platform.node.runtime.migration.external;

import java.util.List;

public interface ExternalMigrationGroupsProvider {
    List<ExternalMigrationGroup> takeExternalMigrationGroups();
}
