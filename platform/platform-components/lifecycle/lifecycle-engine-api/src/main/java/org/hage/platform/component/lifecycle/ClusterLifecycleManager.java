package org.hage.platform.component.lifecycle;

public interface ClusterLifecycleManager {
    void schedulePauseCluster();

    void scheduleStartCluster();
}
