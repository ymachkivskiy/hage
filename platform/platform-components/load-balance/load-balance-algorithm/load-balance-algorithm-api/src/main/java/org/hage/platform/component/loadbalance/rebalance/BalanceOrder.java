package org.hage.platform.component.loadbalance.rebalance;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;

import java.util.List;

@Data
public class BalanceOrder {
    private final NodeAddress orderNode;
    private final List<UnitRelocationOrder> relocationOrders;
}
