package org.hage.platform.component.loadbalance.remote.response;

import org.hage.platform.component.loadbalance.remote.message.LoadBalanceData;
import org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.remote.message.LoadBalancerRemoteMessage;

interface ProcessingStrategy {
    LoadBalanceMessageType getMessageType();

    LoadBalancerRemoteMessage process(LoadBalanceData message);
}
