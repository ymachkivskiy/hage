package org.hage.platform.component.runtime.execution.phase;


import org.hage.platform.component.runtime.execution.ExecutionUnitPhase;

import java.util.List;

public interface ExecutionPhasesProvider {
    List<ExecutionUnitPhase> getExecutionPhasesInOrder();
}
