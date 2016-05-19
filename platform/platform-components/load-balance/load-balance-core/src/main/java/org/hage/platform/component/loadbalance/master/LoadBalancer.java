package org.hage.platform.component.loadbalance.master;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.CorePausedEvent;
import org.hage.platform.component.lifecycle.ClusterLifecycleManager;
import org.hage.platform.component.loadbalance.precondition.ClusterBalanceChecker;
import org.hage.platform.component.loadbalance.precondition.DynamicNodeStats;
import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SingletonComponent
class LoadBalancer implements EventSubscriber {

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


    @SuppressWarnings("unused")
    @Subscribe
    private void performLoadBalancing(CorePausedEvent corePausedEvent) {

        if (activityChecker.isActiveInBalancing()) {

            List<DynamicNodeStats> clusterDynamicStats = balanceManager.getClusterDynamicStats();

            if (!balanceChecker.isBalanced(clusterDynamicStats)) {
                List<BalanceOrder> balanceOrders = balanceCalculator.calculateBalanceOrders(clusterDynamicStats);
                balanceManager.executeBalanceOrders(balanceOrders);
            }


            clusterLifecycleManager.scheduleStartCluster();
        }

    }


}
