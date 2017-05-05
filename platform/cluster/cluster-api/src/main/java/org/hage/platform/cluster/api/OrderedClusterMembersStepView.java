package org.hage.platform.cluster.api;

import java.util.List;

public interface OrderedClusterMembersStepView {
    int membersCount();

    NodeAddress getMember(int number);

    List<NodeAddress> getOrderedMembers();
}
