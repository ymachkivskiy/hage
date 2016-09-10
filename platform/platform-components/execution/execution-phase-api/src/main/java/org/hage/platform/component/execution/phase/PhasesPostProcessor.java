package org.hage.platform.component.execution.phase;

public interface PhasesPostProcessor {
    void afterAllPhasesExecuted(long stepNumber);
}
