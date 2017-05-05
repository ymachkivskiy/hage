package org.hage.platform.cluster.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;

@Data
public class NodeDynamicExecutionInfo {
    private final NodeAddress nodeAddress;
    private final DynamicExecutionInfo dynamicExecutionInfo;
}
