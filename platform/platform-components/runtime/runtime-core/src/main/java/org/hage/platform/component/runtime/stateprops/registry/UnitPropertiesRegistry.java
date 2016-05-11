package org.hage.platform.component.runtime.stateprops.registry;

import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;

public interface UnitPropertiesRegistry {
    ReadWriteUnitProperties unitPropertiesFor(Position position);
}
