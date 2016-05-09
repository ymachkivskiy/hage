package org.hage.platform.component.execution.monitor;

import java.time.Duration;

public interface ExecutionMonitor {
    long getPerformedStepsCount();

    long getCurrentStepNumber();

    Duration getExecutionDuration();
}
