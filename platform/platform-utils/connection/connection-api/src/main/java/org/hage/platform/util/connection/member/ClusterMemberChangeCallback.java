package org.hage.platform.util.connection.member;

import org.hage.platform.util.connection.address.NodeAddress;

public interface ClusterMemberChangeCallback {
    void onMemberAdd(NodeAddress newMember);

    void onMemberRemoved(NodeAddress removedMember);
}
