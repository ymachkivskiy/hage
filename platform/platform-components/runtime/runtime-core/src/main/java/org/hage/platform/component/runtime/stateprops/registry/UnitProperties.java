package org.hage.platform.component.runtime.stateprops.registry;

import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

public interface UnitProperties extends ReadWriteUnitProperties {
    UnitProperties createCopy();
}
