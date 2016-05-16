package org.hage.platform.component.cluster;

public interface OrderedClusterMembersStepView {
    int membersCount();

    NodeAddress getMember(int number);
}
