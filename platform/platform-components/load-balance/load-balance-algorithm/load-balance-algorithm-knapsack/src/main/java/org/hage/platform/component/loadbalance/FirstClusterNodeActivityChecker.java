package org.hage.platform.component.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.LocalClusterNode;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.cluster.OrderedClusterMembersStepView;
import org.hage.platform.component.loadbalance.precondition.LocalNodeLoadBalancerActivityChecker;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
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

        log.debug("Local node is {} active must be first node of cluster {}. Local node is active : {}", localAddress, firstNode, isFirstNode);

        return isFirstNode;
    }

}
