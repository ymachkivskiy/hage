package org.hage.platform.node.execution.phase;

public interface PhasesPostProcessor {
    void afterAllPhasesExecuted(long stepNumber);
}
