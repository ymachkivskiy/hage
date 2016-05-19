package org.hage.platform.component.loadbalance.master;

import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;

import java.util.List;

public interface BalanceManager {
    List<DynamicNodeStats> getClusterDynamicStats();

    void executeBalanceOrders(List<BalanceOrder> orders);
}
