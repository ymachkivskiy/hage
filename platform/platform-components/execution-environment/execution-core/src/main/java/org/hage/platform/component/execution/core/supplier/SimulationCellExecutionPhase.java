package org.hage.platform.component.execution.core.supplier;

import org.hage.platform.component.simulation.structure.SimulationCell;

public enum SimulationCellExecutionPhase {

    CELL_AGENTS_STEP {
        @Override
        public void executeOn(SimulationCell cell) {
            cell.performAgentsStep();
        }
    },

    ;

    abstract void executeOn(SimulationCell cell);
}
