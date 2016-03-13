package org.hage.platform.util.connection;

import java.util.Set;

public interface ClusterAddressManager extends LocalNodeAddressSupplier {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    Set<NodeAddress> getOtherMembersAddresses();

    Set<NodeAddress> getAllAddresses();
}
