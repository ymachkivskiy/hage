package org.hage.platform.component.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;

@Data
public class UnitRelocationOrder implements Serializable {
    private final Position unitToRelocate;
    private final NodeAddress relocationTarget;
}
