package org.hage.platform.component.execution.core.executor;

import org.hage.platform.component.execution.core.supplier.SimulationCellExecutionPhase;

public interface PhasedRunnable {

    void runPhase(SimulationCellExecutionPhase phase);

}
