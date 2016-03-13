package org.hage.platform.component.simulation.structure;

public enum CellExecutionPhase {

    AGENTS_STEP {
        @Override
        public void executeOn(SimulationCell cell) {
            cell.performAgentsStep();
        }
    },

    CONTROL_AGENT_STEP {
        @Override
        public void executeOn(SimulationCell cell) {
            cell.performControlAgentStep();
        }
    };

    public abstract void executeOn(SimulationCell cell);
}
