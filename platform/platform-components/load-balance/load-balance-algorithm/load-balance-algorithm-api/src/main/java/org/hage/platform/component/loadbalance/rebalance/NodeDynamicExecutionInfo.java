package org.hage.platform.component.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;

@Data
public class NodeDynamicExecutionInfo {
    private final NodeAddress nodeAddress;
    private final DynamicExecutionInfo dynamicExecutionInfo;
}
