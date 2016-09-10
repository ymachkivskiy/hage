package org.hage.platform.component.runtime.stateprops.registry;

import java.util.Collection;

public interface UnitPropertiesRefresher {
    void updatePropertiesUsing(Collection<PositionUnitProperties> newProperties);
}
