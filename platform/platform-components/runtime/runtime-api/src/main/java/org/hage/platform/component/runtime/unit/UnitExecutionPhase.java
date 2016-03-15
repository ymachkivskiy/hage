package org.hage.platform.component.runtime.unit;

public enum UnitExecutionPhase {

    AGENTS_STEP {
        @Override
        public void executeOn(ExecutionUnit cell) {
            cell.performAgentsStep();
        }
    },

    CONTROL_AGENT_STEP {
        @Override
        public void executeOn(ExecutionUnit cell) {
            cell.performControlAgentStep();
        }
    };

    public abstract void executeOn(ExecutionUnit cell);
}
