package org.hage.platform.node.executors.schedule;

public interface ContinuousSerialScheduler {
    ScheduledTaskHandle registerTask(ScheduleTask task);

    void shutdown();
}
