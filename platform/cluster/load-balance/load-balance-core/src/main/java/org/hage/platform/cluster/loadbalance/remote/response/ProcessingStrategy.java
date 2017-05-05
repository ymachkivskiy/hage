package org.hage.platform.cluster.loadbalance.remote.response;

import org.hage.platform.cluster.loadbalance.remote.message.LoadBalanceData;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalanceMessageType;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalancerRemoteMessage;

interface ProcessingStrategy {
    LoadBalanceMessageType getMessageType();

    LoadBalancerRemoteMessage process(LoadBalanceData message);
}
