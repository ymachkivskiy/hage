package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.ExecutionCore;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.cluster.loadbalance.LoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;

@SingletonComponent
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ReBalanceLifecycleAction implements LifecycleAction {

    @Autowired
    private ExecutionCore executionCore;
    @Autowired
    private LoadBalancer loadBalancer;

    @Override
    public void execute() {
        log.info("Computation is pausing for re-balancing.");

        executionCore.pause();
        loadBalancer.performReBalancing();

    }
}
