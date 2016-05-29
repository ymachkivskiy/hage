package org.hage.platform.component.runtime.cluster;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.ClusterManager;
import org.hage.platform.component.cluster.ClusterMember;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.cluster.OrderedClusterMembersStepView;
import org.hage.platform.component.execution.step.calback.StepPreProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@SingletonComponent
@Slf4j
class CachedMembersStepView implements OrderedClusterMembersStepView, StepPreProcessor {

    private List<NodeAddress> orderedMembers = emptyList();

    @Autowired
    private ClusterManager clusterManager;

    @Override
    public int membersCount() {
        log.debug("Get member count of members {}", orderedMembers);
        return orderedMembers.size();
    }

    @Override
    public NodeAddress getMember(int number) {
        log.debug("Get member {} from all members {}", number, orderedMembers);
        return orderedMembers.get(number);
    }

    @Override
    public List<NodeAddress> getOrderedMembers() {
        return unmodifiableList(orderedMembers);
    }

    @Override
    public void beforeStepExecuted(long stepNumber) {
        List<ClusterMember> members = clusterManager.getClusterMembers();

        log.debug("Preparing view with members {}", members);

        orderedMembers = members.stream()
            .sorted((m1, m2) -> m1.getMemberIndex() - m2.getMemberIndex())
            .map(ClusterMember::getNodeAddress)
            .collect(toList());
    }
}
