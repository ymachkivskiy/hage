package org.hage.platform.node.runtime.migration.internal;

import org.hage.platform.node.structure.Position;

import java.util.List;

public interface InternalMigrantsProvider {
    List<InternalMigrationGroup> takeMigrationGroups();

    List<InternalMigrationGroup> takeMigrantsTo(Position position);
}
