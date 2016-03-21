package org.hage.platform.component.cluster;

public interface ClusterMemberChangeCallback {
    void onMemberAdd(NodeAddress newMember);

    void onMemberRemoved(NodeAddress removedMember);
}
