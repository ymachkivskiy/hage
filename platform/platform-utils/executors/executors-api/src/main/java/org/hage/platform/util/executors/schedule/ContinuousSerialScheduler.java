package org.hage.platform.util.executors.schedule;

public interface ContinuousSerialScheduler {
    ScheduledTaskHandle registerTask(ScheduleTask task);

    void shutdown();
}
