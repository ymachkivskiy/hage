package org.hage.platform.component.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.monitoring.DynamicStats;

@Data
public class NodeDynamicStats {
    private final NodeAddress nodeAddress;
    private final DynamicStats dynamicStats;
}
