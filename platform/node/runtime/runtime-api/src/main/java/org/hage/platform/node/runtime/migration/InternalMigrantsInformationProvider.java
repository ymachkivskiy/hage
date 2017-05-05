package org.hage.platform.node.runtime.migration;

import org.hage.platform.node.structure.Position;

public interface InternalMigrantsInformationProvider {
    int getNumberOfMigrantsTo(Position position);
}
