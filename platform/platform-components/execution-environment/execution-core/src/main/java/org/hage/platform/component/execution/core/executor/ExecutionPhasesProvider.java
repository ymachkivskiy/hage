package org.hage.platform.component.execution.core.executor;

import org.hage.platform.component.execution.core.supplier.SimulationCellExecutionPhase;

import java.util.List;

public interface ExecutionPhasesProvider {
    List<SimulationCellExecutionPhase> getExecutionPhasesInOrder();
}
