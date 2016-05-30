package org.hage.platform.component.loadbalance.remote;

import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicStats;

import java.util.List;

public interface BalanceManager {
    List<NodeDynamicStats> getClusterDynamicStats();

    void executeBalanceOrders(List<BalanceOrder> orders);
}
