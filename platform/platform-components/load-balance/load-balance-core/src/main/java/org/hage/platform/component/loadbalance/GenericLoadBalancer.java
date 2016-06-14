package org.hage.platform.component.loadbalance;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.ClusterResumer;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.hage.platform.component.loadbalance.remote.BalanceManager;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hage.platform.component.synchronization.SynchPoint.pointForName;

@SingletonComponent
class GenericLoadBalancer implements LoadBalancer {

    @Autowired
    private SynchronizationBarrier barrier;
    @Autowired
    private LocalNodeLoadBalancerActivityChecker activityChecker;
    @Autowired
    private BalanceManager balanceManager;
    @Autowired
    private ClusterBalanceCalculator balanceCalculator;
    @Autowired
    private ClusterResumer clusterResumer;

    @Override
    public void performReBalancing() {

        barrier.synchronize(pointForName("re-balancing"));

        if (activityChecker.isActiveInBalancing()) {

            List<NodeDynamicExecutionInfo> clusterDynamicStats = balanceManager.getClusterDynamicStats();
            List<BalanceOrder> balanceOrders = balanceCalculator.calculateBalanceOrders(clusterDynamicStats);
            balanceManager.executeBalanceOrders(balanceOrders);

            clusterResumer.resumeAfterReBalance();
        }

    }


}
