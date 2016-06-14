package org.hage.platform.component.execution.monitor;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.PhasesPreProcessor;

import java.time.Duration;
import java.util.EnumMap;

import static java.time.Duration.ZERO;

@Slf4j
@SingletonComponent
class StepExecutionDurationInfoProvider implements ExecutionDurationObserver, PhasesPreProcessor {

    private final EnumMap<ExecutionPhaseType, Duration> phaseDurationMap = new EnumMap<>(ExecutionPhaseType.class);
    private Duration stepDuration = ZERO;


    @Override
    public void beforeAllPhasesExecuted(long stepNumber) {
        phaseDurationMap.clear();
    }

    @Override
    public void refreshStepDuration(Duration duration) {
        log.debug("Refresh step duration to {}", duration);
        stepDuration = duration;
    }

    @Override
    public void refreshPhaseDuration(ExecutionPhaseType phaseType, Duration duration) {
        log.debug("Accept phase {} duration {}", phaseType, duration);
        phaseDurationMap.put(phaseType, duration);
    }

    public StepExecutionDurationInfo getStepExecutionDurationInfo() {
        return new StepExecutionDurationInfo(stepDuration, phaseDurationMap);
    }

}
