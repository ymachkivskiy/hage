package org.hage.platform.node.execution.phase;

import java.util.List;

public interface ExecutionPhaseFactory {
    List<ExecutionPhase> getFullCyclePhases();
}
