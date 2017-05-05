package org.hage.platform.node.execution.monitor;

import org.hage.platform.node.execution.phase.ExecutionPhaseType;

import java.time.Duration;

public interface ExecutionDurationObserver {
    void refreshStepDuration(Duration duration);

    void refreshPhaseDuration(ExecutionPhaseType phaseType, Duration duration);
}
