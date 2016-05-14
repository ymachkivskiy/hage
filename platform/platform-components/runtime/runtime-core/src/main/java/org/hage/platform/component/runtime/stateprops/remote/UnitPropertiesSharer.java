package org.hage.platform.component.runtime.stateprops.remote;

import org.hage.platform.component.runtime.stateprops.registry.PositionUnitProperties;

import java.util.List;

public interface UnitPropertiesSharer {
    void shareUpdatedProperties(List<PositionUnitProperties> unitProperties);
}
