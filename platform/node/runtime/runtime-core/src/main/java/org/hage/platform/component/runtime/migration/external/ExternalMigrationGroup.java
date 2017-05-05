package org.hage.platform.component.runtime.migration.external;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;

import java.util.List;

@Data
public class ExternalMigrationGroup {
    private final NodeAddress targetNode;
    private final List<InternalMigrationGroup> migrationGroups;
}
