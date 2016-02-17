package org.hage.platform.component.execution.core.supplier;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;
import org.hage.platform.component.simulation.structure.SimulationCell;

@RequiredArgsConstructor
class CellPhaseRunnableAdapter implements PhasedRunnable {

    private final SimulationCell simulationCell;

    @Override
    public void runPhase(SimulationCellExecutionPhase phase) {
        phase.executeOn(simulationCell);
    }

}
