package org.hage.platform.component.runtime.execution;

public enum ExecutionUnitPhase {

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
    },
    CLEANUP {
        @Override
        public void executeOn(ExecutionUnit cell) {
            cell.afterStepPerformed();
        }
    };

    public abstract void executeOn(ExecutionUnit cell);
}
