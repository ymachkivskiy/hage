package org.hage.platform.component.cluster;

import java.util.Set;

public interface ClusterAddressManager extends LocalClusterNode {
    Set<NodeAddress> getOtherMembersAddresses();

    Set<NodeAddress> getAllAddresses();
}
