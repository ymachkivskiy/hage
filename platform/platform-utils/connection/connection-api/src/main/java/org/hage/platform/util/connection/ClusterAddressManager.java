package org.hage.platform.util.connection;

import java.util.Set;

public interface ClusterAddressManager extends LocalNodeAddressProvider {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    Set<NodeAddress> getOtherMembersAddresses();
}
