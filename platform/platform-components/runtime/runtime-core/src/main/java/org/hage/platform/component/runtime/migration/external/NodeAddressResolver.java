package org.hage.platform.component.runtime.migration.external;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.runtime.cluster.ClusterMembersStepView;
import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Math.abs;

@SingletonComponent
public class NodeAddressResolver {

    @Autowired
    private ClusterMembersStepView clusterMembersStepView;

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
