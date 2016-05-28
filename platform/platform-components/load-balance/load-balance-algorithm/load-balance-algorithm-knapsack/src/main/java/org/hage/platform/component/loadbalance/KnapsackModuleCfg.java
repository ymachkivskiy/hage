package org.hage.platform.component.loadbalance;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.component.loadbalance.precondition.ClusterBalanceChecker;
import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
class KnapsackModuleCfg {

    @Bean
    public LocalNodeLoadBalancerActivityChecker firstClusterNodeMasterResolver() {
        return new FirstClusterNodeActivityChecker();
    }

    @Bean
    public ClusterBalanceChecker clusterBalanceChecker() {
        return new DummyBalanceChecker();
    }

    @Bean
    public ClusterBalanceCalculator clusterBalanceCalculator() {
        return new DummyBalanceCalculator();
    }

}
