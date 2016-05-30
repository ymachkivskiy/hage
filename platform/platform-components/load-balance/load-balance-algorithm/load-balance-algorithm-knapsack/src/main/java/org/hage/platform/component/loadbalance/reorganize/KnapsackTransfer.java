package org.hage.platform.component.loadbalance.reorganize;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.platform.component.loadbalance.knapsack.PositionItem;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;


@RequiredArgsConstructor
@ToString
public class KnapsackTransfer {
    private final Knapsack origin;
    private final PositionItem positionItem;
    private final Knapsack destination;

    public NodeAddress getOriginNode() {
        return origin.getAddress();
    }

    public UnitRelocationOrder toRelocationOrder() {
        return new UnitRelocationOrder(positionItem.getPosition(), destination.getAddress());
    }

}
