package org.hage.platform.node.runtime.stateprops.registry;

import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

public interface UnitProperties extends ReadWriteUnitProperties {
    UnitProperties createCopy();
}
