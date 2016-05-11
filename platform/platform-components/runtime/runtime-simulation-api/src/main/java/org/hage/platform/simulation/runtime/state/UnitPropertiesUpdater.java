package org.hage.platform.simulation.runtime.state;

import org.hage.platform.component.structure.Position;

import java.io.Serializable;

public interface UnitPropertiesUpdater extends Serializable {
    void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber);
}
