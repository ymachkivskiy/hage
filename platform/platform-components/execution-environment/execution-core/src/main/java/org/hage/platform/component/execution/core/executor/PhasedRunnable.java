package org.hage.platform.component.execution.core.executor;


import org.hage.platform.component.runtime.unit.UnitExecutionPhase;

public interface PhasedRunnable {

    void runPhase(UnitExecutionPhase phase);

}
