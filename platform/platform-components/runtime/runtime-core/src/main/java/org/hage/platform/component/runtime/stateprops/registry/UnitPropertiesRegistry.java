package org.hage.platform.component.runtime.stateprops.registry;

import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.property.ReadUnitProperties;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

public interface UnitPropertiesRegistry {
    ReadWriteUnitProperties readWriteUnitPropertiesFor(Position position);

    ReadUnitProperties readUnitPropertiesFor(Position position);
}
