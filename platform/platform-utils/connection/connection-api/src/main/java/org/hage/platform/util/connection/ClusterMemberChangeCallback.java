package org.hage.platform.util.connection;

import org.hage.platform.communication.address.NodeAddress;

public interface ClusterMemberChangeCallback {
    void onMemberAdd(NodeAddress newMember);

    void onMemberRemoved(NodeAddress removedMember);
}
