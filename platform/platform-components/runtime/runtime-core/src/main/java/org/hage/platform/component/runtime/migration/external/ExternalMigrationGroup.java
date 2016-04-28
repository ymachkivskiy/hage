package org.hage.platform.component.runtime.migration.external;

import lombok.Data;
import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;

import java.util.List;

@Data
public class ExternalMigrationGroup {
    private final AgentsUnitAddress unitAddress;
    private final List<InternalMigrationGroup> migrationGroups;
}
