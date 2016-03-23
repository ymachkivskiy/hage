package org.hage.platform.component.structure.address;

import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.connections.Position;

public interface StructureAddressingConfigurator {
    void bindWithAddress(Position position, NodeAddress address);

    void deactivate(Position position);
}
