package org.hage.platform.component.loadbalance.core.remote.process.strategy;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceData;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.AsynchronousLifecycleCommand.PAUSE;
import static org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType.PAUSE_NODE;
import static org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage.ackMessage;

@Slf4j
@SingletonComponent
class PauseNodeProcessingStrategy implements ProcessingStrategy {

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Override
    public LoadBalanceMessageType getMessageType() {
        return PAUSE_NODE;
    }

    @Override
    public LoadBalancerRemoteMessage process(LoadBalanceData data) {
        log.info("Got request for pausing local node");
        lifecycleCommandInvoker.invokeCommand(PAUSE);
        return ackMessage();
    }

}
