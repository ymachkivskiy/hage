package org.hage.platform.component.execution.core.executor;

import org.hage.platform.component.simulation.structure.CellExecutionPhase;

public interface PhasedRunnable {

    void runPhase(CellExecutionPhase phase);

}
