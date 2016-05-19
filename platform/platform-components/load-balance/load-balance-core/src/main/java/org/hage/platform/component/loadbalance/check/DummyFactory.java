package org.hage.platform.component.loadbalance.check;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;

// TODO: reimplement
@SingletonComponent
@Slf4j
class DummyFactory implements BalanceCheckStrategyFactory {

    @Override
    public BalanceCheckStrategy buildStrategyForConfig(LoadBalanceConfig config) {
        log.debug("Building dummy strategy");
        return () -> false;
    }

}
