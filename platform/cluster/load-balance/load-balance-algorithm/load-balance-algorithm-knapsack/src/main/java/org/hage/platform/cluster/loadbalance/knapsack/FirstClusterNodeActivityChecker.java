package org.hage.platform.cluster.loadbalance.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.api.OrderedClusterMembersStepView;
import org.hage.platform.cluster.loadbalance.LocalNodeLoadBalancerActivityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirstClusterNodeActivityChecker implements LocalNodeLoadBalancerActivityChecker {

    @Autowired
    private OrderedClusterMembersStepView clusterMembersStepView;
    @Autowired
    private LocalClusterNode localClusterNode;

    @Override
    public boolean isActiveInBalancing() {
        NodeAddress localAddress = localClusterNode.getLocalAddress();
        NodeAddress firstNode = clusterMembersStepView.getMember(0);
        boolean isFirstNode = firstNode.equals(localAddress);

        log.debug("Local node is \"{}\" active must be first node of cluster \"{}\". Local node is active : {}", localAddress, firstNode, isFirstNode);

        return isFirstNode;
    }

}
