package org.hage.platform.component.structure.distribution;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.structure.Position;

@Data
public class ActivePositionAddress {
    private final Position position;
    private final NodeAddress nodeAddress;
}
