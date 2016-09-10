package org.hage.platform.component.loadbalance.check;

import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;

public interface BalanceCheckStrategyFactory {
    BalanceCheckStrategy buildStrategyForConfig(LoadBalanceConfig config);
}
