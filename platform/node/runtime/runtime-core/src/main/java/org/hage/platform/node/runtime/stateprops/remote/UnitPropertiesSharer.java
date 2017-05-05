package org.hage.platform.node.runtime.stateprops.remote;

import org.hage.platform.node.runtime.stateprops.registry.PositionUnitProperties;

import java.util.List;

public interface UnitPropertiesSharer {
    void shareUpdatedProperties(List<PositionUnitProperties> unitProperties);
}
