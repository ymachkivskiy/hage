package org.hage.platform.cluster.api;

public interface ClusterMemberChangeCallback {
    default void onMemberAdd(NodeAddress newMember) {
    }

    default void onMemberRemoved(NodeAddress removedMember) {

    }
}
