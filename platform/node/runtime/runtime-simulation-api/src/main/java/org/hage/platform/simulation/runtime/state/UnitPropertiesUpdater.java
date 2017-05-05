package org.hage.platform.simulation.runtime.state;

import org.hage.platform.node.structure.Position;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

import java.io.Serializable;

public interface UnitPropertiesUpdater extends Serializable {
    void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber);
}
