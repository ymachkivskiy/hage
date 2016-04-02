package org.hage.platform.component.runtime.execution.phase;


import org.hage.platform.component.runtime.execution.ExecutionUnitPhase;

public interface PhasedRunnable {

    void runPhase(ExecutionUnitPhase phase);

}
