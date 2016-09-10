package org.hage.platform.component.execution.phase;

import java.util.List;

public interface ExecutionPhaseFactory {
    List<ExecutionPhase> getFullCyclePhases();
}
