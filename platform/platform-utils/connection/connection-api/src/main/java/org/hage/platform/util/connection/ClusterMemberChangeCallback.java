package org.hage.platform.util.connection;

public interface ClusterMemberChangeCallback {
    void onMemberAdd(NodeAddress newMember);

    void onMemberRemoved(NodeAddress removedMember);
}
