package org.hage.platform.component.execution.monitor;

import java.time.Duration;

public interface SimulationExecutionMonitor {
    long getPerformedStepsCount();

    long getCurrentStepNumber();

    Duration getSimulationExecutionDuration();
}
