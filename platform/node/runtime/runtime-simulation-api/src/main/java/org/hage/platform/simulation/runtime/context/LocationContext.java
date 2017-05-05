package org.hage.platform.simulation.runtime.context;

import org.hage.platform.node.structure.connections.Neighbors;
import org.hage.platform.node.structure.connections.UnitAddress;

public interface LocationContext {
    UnitAddress queryLocalUnit();

    Neighbors querySurroundingUnits();
}
