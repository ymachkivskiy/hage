package org.hage.platform.component.loadbalance.master;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.calback.StepPostProcessor;
import org.hage.platform.component.lifecycle.ClusterLifecycleManager;
import org.hage.platform.component.loadbalance.check.BalanceCheckStrategy;
import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;


@Order(0)
@Slf4j
@SingletonComponent
class LoadBalancerInvoker implements StepPostProcessor {

    private BalanceCheckStrategy checkStrategy;

    @Autowired
    private LocalNodeLoadBalancerActivityChecker activityChecker;
    @Autowired
    private ClusterLifecycleManager clusterLifecycleManager;

    @Override
    public void afterStepPerformed(long stepNumber) {
        log.info("Check if load balancer should be invoked in step {}", stepNumber);
        if (activityChecker.isActiveInBalancing() && checkStrategy.shouldCheckBalance()) {
            clusterLifecycleManager.schedulePauseCluster();
        }
    }

    void setCheckStrategy(BalanceCheckStrategy checkStrategy) {
        this.checkStrategy = checkStrategy;
    }

}
