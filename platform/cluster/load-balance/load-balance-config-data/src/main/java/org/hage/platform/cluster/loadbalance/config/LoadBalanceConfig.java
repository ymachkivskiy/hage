package org.hage.platform.cluster.loadbalance.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoadBalanceConfig implements Serializable {
    private final BalanceMode mode;
    private final int amount;

    public boolean isEnabled() {
        return mode != BalanceMode.DISABLED;
    }
}
