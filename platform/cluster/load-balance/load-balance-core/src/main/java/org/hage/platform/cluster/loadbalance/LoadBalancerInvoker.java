package org.hage.platform.cluster.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.PhasesPostProcessor;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.cluster.loadbalance.check.BalanceCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.ASYNC__PAUSE_FOR_RE_BALANCE;


@Order(0)
@Slf4j
@SingletonComponent
class LoadBalancerInvoker implements PhasesPostProcessor {

    private BalanceCheckStrategy checkStrategy;

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Override
    public void afterAllPhasesExecuted(long stepNumber) {
        log.info("Check if load balancer should be invoked in step {}", stepNumber);

        if (checkStrategy.shouldCheckBalance()) {
            lifecycleCommandInvoker.invokeCommand(ASYNC__PAUSE_FOR_RE_BALANCE);
        }
    }

    void setCheckStrategy(BalanceCheckStrategy checkStrategy) {
        this.checkStrategy = checkStrategy;
    }

}
