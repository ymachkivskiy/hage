package org.hage.platform.component.loadbalance;

import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;

// TODO: rename
public class FirstClusterNodeOrchestratorResolver implements LocalNodeLoadBalancerActivityChecker {

    @Override
    public boolean isActiveInBalancing() {
        //todo : NOT IMPLEMENTED
        return false;
    }

}
