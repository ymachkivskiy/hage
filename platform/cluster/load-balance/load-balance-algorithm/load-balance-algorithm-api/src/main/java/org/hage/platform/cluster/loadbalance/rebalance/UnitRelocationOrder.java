package org.hage.platform.cluster.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;

@Data
public class UnitRelocationOrder implements Serializable {
    private final Position unitToRelocate;
    private final NodeAddress relocationTarget;
}
