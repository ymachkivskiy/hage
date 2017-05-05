package org.hage.platform.component.loadbalance.remote.message;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
@Data
public class LoadBalancerRemoteMessage implements Serializable {
    private final LoadBalanceMessageType type;
    private final LoadBalanceData data;
}
