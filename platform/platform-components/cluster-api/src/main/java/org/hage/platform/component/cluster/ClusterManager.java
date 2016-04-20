package org.hage.platform.component.cluster;

import java.util.Set;

public interface ClusterManager extends LocalNodeAddressSupplier {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    Set<NodeAddress> getOtherMembersAddresses();

    Set<NodeAddress> getAllAddresses();

    int getMembersCount();
}
