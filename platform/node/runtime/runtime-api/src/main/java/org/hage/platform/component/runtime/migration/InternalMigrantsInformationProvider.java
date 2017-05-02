package org.hage.platform.component.runtime.migration;

import org.hage.platform.component.structure.Position;

public interface InternalMigrantsInformationProvider {
    int getNumberOfMigrantsTo(Position position);
}
