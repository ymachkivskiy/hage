package org.hage.platform.component.execution.core.executor;

import org.hage.platform.component.simulation.structure.CellExecutionPhase;

import java.util.List;

public interface ExecutionPhasesProvider {
    List<CellExecutionPhase> getExecutionPhasesInOrder();
}
