package org.hage.platform.component.cluster;

import java.util.List;

public interface ClusterManager extends  ClusterAddressManager {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    List<ClusterMember> getClusterMembers();

    int getMembersCount();
}
