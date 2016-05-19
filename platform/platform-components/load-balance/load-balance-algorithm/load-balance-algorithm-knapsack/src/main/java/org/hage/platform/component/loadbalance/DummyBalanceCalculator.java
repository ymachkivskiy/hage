package org.hage.platform.component.loadbalance;

import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;

import java.util.Collections;
import java.util.List;

public class DummyBalanceCalculator implements ClusterBalanceCalculator {

    @Override
    public List<BalanceOrder> calculateBalanceOrders(List<DynamicNodeStats> stats) {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }

}
