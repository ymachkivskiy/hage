package org.hage.platform.component.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;

@Slf4j
public class LoadBalanceCoreConfigurer extends CoreConfigurerAdapter<LoadBalanceConfig> {

    public LoadBalanceCoreConfigurer(int order) {
        super(config -> config.getCommon().getLoadBalanceConfig(), order);
    }

    @Override
    protected void configureWithNarrow(LoadBalanceConfig narrowConfiguration) {
        log.debug("Configure load balancer.");
        //todo : NOT IMPLEMENTED

    }

}
