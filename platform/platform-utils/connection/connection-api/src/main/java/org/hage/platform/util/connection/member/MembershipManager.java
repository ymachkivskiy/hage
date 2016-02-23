package org.hage.platform.util.connection.member;

import org.hage.platform.util.connection.address.LocalNodeAddressProvider;
import org.hage.platform.util.connection.address.NodeAddress;

import java.util.Set;

public interface MembershipManager extends LocalNodeAddressProvider {
    void addMembershipChangeCallback(ClusterMemberChangeCallback callback);

    Set<NodeAddress> getOtherMembersAddresses();

}
