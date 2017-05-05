package org.hage.platform.node.execution.monitor;

import java.time.Duration;

public interface SimulationExecutionMonitor {
    long getPerformedStepsCount();

    long getCurrentStepNumber();

    Duration getSimulationExecutionDuration();
}
