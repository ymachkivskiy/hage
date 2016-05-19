package org.hage.platform.component.loadbalance;

import org.hage.platform.component.loadbalance.precondition.ClusterBalanceChecker;
import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;

import java.util.List;

public class DummyBalanceChecker implements ClusterBalanceChecker {
    @Override
    public boolean isBalanced(List<DynamicNodeStats> dynamicNodeStats) {
        //todo : NOT IMPLEMENTED
        return true;
    }
}
