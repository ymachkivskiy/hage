package org.hage.platform.component.loadbalance.balancing;

import java.util.Collection;

public interface Balancer {
    Collection<KnapsackTransfer> balance(BalancingInput balancingInput);
}
