package org.hage.platform.util.executors.schedule;

public interface ScheduledTaskHandle {
    void cancelAndWaitToComplete();

    void resumeIfNotRunning();
}