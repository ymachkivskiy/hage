package org.hage.platform.component.execution.core.executor;


import org.hage.platform.component.runtime.unit.UnitExecutionPhase;

import java.util.List;

public interface ExecutionPhasesProvider {
    List<UnitExecutionPhase> getExecutionPhasesInOrder();
}
