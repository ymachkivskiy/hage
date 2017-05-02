package org.hage.platform.simulation.runtime.context;

import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;

public interface LocationContext {
    UnitAddress queryLocalUnit();

    Neighbors querySurroundingUnits();
}
