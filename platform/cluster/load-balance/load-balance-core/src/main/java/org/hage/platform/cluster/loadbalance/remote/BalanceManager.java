package org.hage.platform.cluster.loadbalance.remote;

import org.hage.platform.cluster.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.cluster.loadbalance.rebalance.NodeDynamicExecutionInfo;

import java.util.List;

public interface BalanceManager {
    List<NodeDynamicExecutionInfo> getClusterDynamicStats();

    void executeBalanceOrders(List<BalanceOrder> orders);
}
