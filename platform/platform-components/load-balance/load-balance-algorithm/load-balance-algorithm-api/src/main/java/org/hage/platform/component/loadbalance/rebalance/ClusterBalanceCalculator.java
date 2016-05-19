package org.hage.platform.component.loadbalance.rebalance;

import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;

import java.util.List;

public interface ClusterBalanceCalculator {
    // TODO: document
    List<BalanceOrder> calculateBalanceOrders(List<DynamicNodeStats> stats);
}
