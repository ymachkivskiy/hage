package org.hage.platform.component.lifecycle;

public interface ClusterLifecycleManager {
    void pauseForReBalance();

    void resumeAfterReBalance();
}
