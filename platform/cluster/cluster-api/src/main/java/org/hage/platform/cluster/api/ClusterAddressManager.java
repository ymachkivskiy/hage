package org.hage.platform.cluster.api;

import java.util.Set;

public interface ClusterAddressManager extends LocalClusterNode {
    Set<NodeAddress> getOtherMembersAddresses();

    Set<NodeAddress> getAllAddresses();
}
