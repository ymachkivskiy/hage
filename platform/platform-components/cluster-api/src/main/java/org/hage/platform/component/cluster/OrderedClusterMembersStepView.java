package org.hage.platform.component.cluster;

import java.util.List;

public interface OrderedClusterMembersStepView {
    int membersCount();

    NodeAddress getMember(int number);

    List<NodeAddress> getOrderedMembers();
}
