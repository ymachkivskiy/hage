package org.hage.platform.component.loadbalance.remote;

import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicExecutionInfo;

import java.util.List;

public interface BalanceManager {
    List<NodeDynamicExecutionInfo> getClusterDynamicStats();

    void executeBalanceOrders(List<BalanceOrder> orders);
}
