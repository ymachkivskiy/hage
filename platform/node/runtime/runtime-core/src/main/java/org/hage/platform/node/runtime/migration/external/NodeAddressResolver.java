package org.hage.platform.node.runtime.migration.external;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.api.OrderedClusterMembersStepView;
import org.hage.platform.node.runtime.location.AgentsUnitAddress;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Math.abs;

@SingletonComponent
public class NodeAddressResolver {

    @Autowired
    private OrderedClusterMembersStepView clusterMembersStepView;

    public NodeAddress resolveFor(AgentsUnitAddress unitAddress){
        if (unitAddress.isOnline()) {
            return unitAddress.getNodeAddress();
        }
        else
        {
            int positionHash = abs(unitAddress.getPosition().hashCode());
            int allCount = clusterMembersStepView.membersCount();
            return clusterMembersStepView.getMember(positionHash % allCount);
        }
    }
}
