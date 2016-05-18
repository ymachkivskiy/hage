package org.hage.platform.component.loadbalance.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.check.BalanceCheckStrategyFactory;
import org.hage.platform.component.loadbalance.check.BalancePreCheckStrategy;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LoadBalanceCoreConfigurer extends CoreConfigurerAdapter<LoadBalanceConfig> {

    @Autowired
    private BalanceCheckStrategyFactory checkStrategyFactory;
    @Autowired
    private LoadBalancerInvoker loadBalancerInvoker;

    public LoadBalanceCoreConfigurer(int order) {
        super(config -> config.getCommon().getLoadBalanceConfig(), order);
    }

    @Override
    protected void configureWithNarrow(LoadBalanceConfig balanceConfig) {
        log.debug("Configure load balancer with {}", balanceConfig);

        BalancePreCheckStrategy checkStrategy = checkStrategyFactory.buildStrategyForConfig(balanceConfig);
        loadBalancerInvoker.setCheckStrategy(checkStrategy);
    }

}
