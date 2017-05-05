package org.hage.platform.node.execution.step;

import org.hage.platform.node.executors.schedule.ScheduleTask;

public interface StepTask extends ScheduleTask {
    void reset();
}
