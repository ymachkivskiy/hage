package org.hage.platform.component.loadbalance;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.ClusterLifecycleManager;
import org.hage.platform.component.loadbalance.master.BalanceManager;
import org.hage.platform.component.loadbalance.precondition.ClusterBalanceChecker;
import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;
import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SingletonComponent
class GenericLoadBalancer implements LoadBalancer {

    @Autowired
    private ClusterLifecycleManager clusterLifecycleManager;
    @Autowired
    private BalanceManager balanceManager;
    @Autowired
    private ClusterBalanceChecker balanceChecker;
    @Autowired
    private ClusterBalanceCalculator balanceCalculator;
    @Autowired
    private LocalNodeLoadBalancerActivityChecker activityChecker;

    @Override
    public void performReBalancing() {

        if (activityChecker.isActiveInBalancing()) {

            List<DynamicNodeStats> clusterDynamicStats = balanceManager.getClusterDynamicStats();

            if (!balanceChecker.isBalanced(clusterDynamicStats)) {
                List<BalanceOrder> balanceOrders = balanceCalculator.calculateBalanceOrders(clusterDynamicStats);
                balanceManager.executeBalanceOrders(balanceOrders);
            }


            clusterLifecycleManager.resumeAfterReBalance();
        }

    }


}
