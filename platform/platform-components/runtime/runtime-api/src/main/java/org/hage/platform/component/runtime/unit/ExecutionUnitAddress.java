package org.hage.platform.component.runtime.unit;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.definition.Position;
import org.hage.platform.simulation.identification.UnitAddress;

@Data
public class ExecutionUnitAddress implements UnitAddress {
    private final NodeAddress nodeAddress;
    private final Position position;

    @Override
    public String getUniqueIdentifier() {
        return "[" + nodeAddress + "]::" + position;
    }

    @Override
    public String getFriendlyName() {
        return position.toString();
    }
}
