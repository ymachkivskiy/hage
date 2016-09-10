package org.hage.platform.component.execution.step;

import org.hage.platform.util.executors.schedule.ScheduleTask;

public interface StepTask extends ScheduleTask {
    void reset();
}
