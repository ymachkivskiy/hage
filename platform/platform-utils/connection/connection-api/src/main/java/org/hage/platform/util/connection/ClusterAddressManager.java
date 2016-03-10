package org.hage.platform.util.connection;

import org.hage.platform.communication.address.LocalNodeAddressSupplier;
import org.hage.platform.communication.address.NodeAddress;

import java.util.Set;

public interface ClusterAddressManager extends LocalNodeAddressSupplier {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    Set<NodeAddress> getOtherMembersAddresses();

    Set<NodeAddress> getAllAddresses();
}
