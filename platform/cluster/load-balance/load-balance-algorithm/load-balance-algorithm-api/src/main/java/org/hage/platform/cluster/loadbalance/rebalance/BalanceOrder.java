package org.hage.platform.cluster.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;

import java.util.List;

@Data
public class BalanceOrder {
    private final NodeAddress orderNode;
    private final List<UnitRelocationOrder> relocationOrders;
}
