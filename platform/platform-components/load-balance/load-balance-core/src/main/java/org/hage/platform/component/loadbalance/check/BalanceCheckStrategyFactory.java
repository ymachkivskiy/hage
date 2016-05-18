package org.hage.platform.component.loadbalance.check;

import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;

public interface BalanceCheckStrategyFactory {
    BalancePreCheckStrategy buildStrategyForConfig(LoadBalanceConfig config);
}
