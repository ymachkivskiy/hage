package org.hage.platform.component.runtime.cluster;

import org.hage.platform.component.cluster.NodeAddress;

public interface ClusterMembersStepView {
    int membersCount();

    NodeAddress getMember(int number);
}
