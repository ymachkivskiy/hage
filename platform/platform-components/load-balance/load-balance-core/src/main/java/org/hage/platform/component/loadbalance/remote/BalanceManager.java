package org.hage.platform.component.loadbalance.remote;

import org.hage.platform.component.loadbalance.precondition.NodeDynamicStats;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;

import java.util.List;

public interface BalanceManager {
    List<NodeDynamicStats> getClusterDynamicStats();

    void executeBalanceOrders(List<BalanceOrder> orders);
}
