package org.hage.platform.component.runtime.execution.change;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.execution.ExecutionUnit;
import org.hage.platform.component.runtime.execution.ExecutionUnitPhase;
import org.hage.platform.component.runtime.execution.phase.PhasedRunnable;

@RequiredArgsConstructor
class CellPhaseRunnableAdapter implements PhasedRunnable {

    private final ExecutionUnit executionUnit;

    @Override
    public void runPhase(ExecutionUnitPhase phase) {
        phase.executeOn(executionUnit);
    }

    @Override
    public String toString() {
        return "Execution unit [" + executionUnit.getUnitId() + "] runnable";
    }
}
