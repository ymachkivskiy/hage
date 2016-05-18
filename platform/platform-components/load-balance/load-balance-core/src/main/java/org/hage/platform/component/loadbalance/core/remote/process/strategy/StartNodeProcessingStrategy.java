package org.hage.platform.component.loadbalance.core.remote.process.strategy;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceData;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.AsynchronousLifecycleCommand.START;
import static org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType.START_NODE;
import static org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage.ackMessage;

@Slf4j
@SingletonComponent
class StartNodeProcessingStrategy implements ProcessingStrategy {

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Override
    public LoadBalanceMessageType getMessageType() {
        return START_NODE;
    }

    @Override
    public LoadBalancerRemoteMessage process(LoadBalanceData message) {
        log.info("Got request for restart node");

        lifecycleCommandInvoker.invokeCommand(START);
        return ackMessage();
    }
}
