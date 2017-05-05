package org.hage.platform.component.cluster;

public interface ClusterMemberChangeCallback {
    default void onMemberAdd(NodeAddress newMember) {
    }

    default void onMemberRemoved(NodeAddress removedMember) {

    }
}
