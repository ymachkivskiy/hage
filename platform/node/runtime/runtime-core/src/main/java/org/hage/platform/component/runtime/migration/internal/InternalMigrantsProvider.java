package org.hage.platform.component.runtime.migration.internal;

import org.hage.platform.component.structure.Position;

import java.util.List;

public interface InternalMigrantsProvider {
    List<InternalMigrationGroup> takeMigrationGroups();

    List<InternalMigrationGroup> takeMigrantsTo(Position position);
}
