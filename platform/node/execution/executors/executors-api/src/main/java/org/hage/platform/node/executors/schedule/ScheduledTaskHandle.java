package org.hage.platform.node.executors.schedule;

public interface ScheduledTaskHandle {
    void cancelAndWaitToComplete();

    void resumeIfNotRunning();
}