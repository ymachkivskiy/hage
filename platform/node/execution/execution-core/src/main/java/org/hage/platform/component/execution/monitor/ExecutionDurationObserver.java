package org.hage.platform.component.execution.monitor;

import org.hage.platform.component.execution.phase.ExecutionPhaseType;

import java.time.Duration;

public interface ExecutionDurationObserver {
    void refreshStepDuration(Duration duration);

    void refreshPhaseDuration(ExecutionPhaseType phaseType, Duration duration);
}
