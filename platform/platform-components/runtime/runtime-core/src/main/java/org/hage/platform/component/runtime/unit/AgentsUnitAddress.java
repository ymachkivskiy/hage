package org.hage.platform.component.runtime.unit;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.UnitAddress;

@Data
public class AgentsUnitAddress implements UnitAddress {
    private final NodeAddress nodeAddress;
    private final Position position;

    @Override
    public String getUniqueIdentifier() {
        return "[" + nodeAddress + "]::" + position;
    }

}
