package org.hage.platform.component.runtime.step.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.runtime.unit.Unit;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.MAIN___CONTROL_AGENT_STEP;

@Slf4j
@SingletonComponent
public class ControlAgentStepPhase extends AbstractUnitPhase {

    @Override
    public ExecutionPhaseType getType() {
        return MAIN___CONTROL_AGENT_STEP;
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::runControlAgent;
    }

}
