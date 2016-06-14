package org.hage.platform.component.loadbalance.rebalance;

import java.util.List;

public interface ClusterBalanceCalculator {
    // TODO: document
    List<BalanceOrder> calculateBalanceOrders(List<NodeDynamicExecutionInfo> stats);
}
