package org.hage.platform.cluster.loadbalance.check;

public interface BalanceCheckStrategy {
    boolean shouldCheckBalance();
}
