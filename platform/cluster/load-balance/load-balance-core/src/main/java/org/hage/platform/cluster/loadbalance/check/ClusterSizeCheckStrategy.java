package org.hage.platform.cluster.loadbalance.check;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.OrderedClusterMembersStepView;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SingletonComponent
class ClusterSizeCheckStrategy implements BalanceCheckStrategy {

    @Autowired
    private OrderedClusterMembersStepView clusterMembersStepView;

    @Override
    public boolean shouldCheckBalance() {
        boolean containsMoreThanOneNode = clusterMembersStepView.membersCount() > 1;

        log.info("Check if cluster contains more than one node: {}", containsMoreThanOneNode);

        return containsMoreThanOneNode;
    }

}
