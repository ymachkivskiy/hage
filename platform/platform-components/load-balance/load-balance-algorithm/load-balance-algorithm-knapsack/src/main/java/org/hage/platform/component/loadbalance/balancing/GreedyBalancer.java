package org.hage.platform.component.loadbalance.balancing;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;

@Slf4j
public class GreedyBalancer implements Balancer {

    @Override
    public Collection<KnapsackTransfer> balance(BalancingInput balancingInput) {

        //todo : NOT IMPLEMENTED
        return Collections.emptyList();

    }


}
