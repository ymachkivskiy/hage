package org.hage.platform.component.runtime.stateprops.registry;

import java.util.Collection;

public interface UnitPropertiesUpdater {
    void updatePropertiesUsing(Collection<PositionUnitProperties> newProperties);
}
