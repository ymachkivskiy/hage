package org.hage.platform.component.loadbalance.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.calback.StepPostProcessor;
import org.hage.platform.component.loadbalance.check.BalancePreCheckStrategy;
import org.hage.platform.component.loadbalance.core.master.LoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;


@Order(0)
@Slf4j
@SingletonComponent
class LoadBalancerInvoker implements StepPostProcessor {

    @Autowired
    private LoadBalancer loadBalancer;

    private BalancePreCheckStrategy checkStrategy;

    @Override
    public void afterStepPerformed(long stepNumber) {
        log.info("Check if load balancer should be invoked in step {}", stepNumber);
        if (checkStrategy.shouldCheck()) {
            loadBalancer.performLoadBalancing();
        }
    }

    void setCheckStrategy(BalancePreCheckStrategy checkStrategy) {
        this.checkStrategy = checkStrategy;
    }

}
