package org.hage.platform.component.execution.core.supplier;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;
import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.component.runtime.unit.UnitExecutionPhase;

@RequiredArgsConstructor
class CellPhaseRunnableAdapter implements PhasedRunnable {

    private final ExecutionUnit executionUnit;

    @Override
    public void runPhase(UnitExecutionPhase phase) {
        phase.executeOn(executionUnit);
    }

}
