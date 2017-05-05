package org.hage.platform.cluster.loadbalance.check;

import org.hage.platform.cluster.loadbalance.config.LoadBalanceConfig;

public interface BalanceCheckStrategyFactory {
    BalanceCheckStrategy buildStrategyForConfig(LoadBalanceConfig config);
}
