package org.hage.platform.component.runtime.unit;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.simulation.runtime.UnitAddress;

@Data
public class ExecutionUnitAddress implements UnitAddress {
    private final NodeAddress nodeAddress;
    private final Position position;

    @Override
    public String getUniqueIdentifier() {
        return "[" + nodeAddress + "]::"  /* + position*/;
    }
    // TODO: work with
}
