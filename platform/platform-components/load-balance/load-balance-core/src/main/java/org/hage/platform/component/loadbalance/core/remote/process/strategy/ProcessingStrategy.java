package org.hage.platform.component.loadbalance.core.remote.process.strategy;

import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceData;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage;

public interface ProcessingStrategy {
    LoadBalanceMessageType getMessageType();

    LoadBalancerRemoteMessage process(LoadBalanceData message);
}
