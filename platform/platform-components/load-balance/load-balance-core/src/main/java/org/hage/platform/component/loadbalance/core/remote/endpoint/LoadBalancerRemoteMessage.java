package org.hage.platform.component.loadbalance.core.remote.endpoint;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoadBalancerRemoteMessage implements Serializable {
    private final LoadBalanceMessageType type;
    private final LoadBalanceData data;

    public static LoadBalancerRemoteMessage ackMessage() {
        return new LoadBalancerRemoteMessage(LoadBalanceMessageType.ACK, null);
    }

}
