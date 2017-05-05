package org.hage.platform.node.runtime.step.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.phase.ExecutionPhaseType;
import org.hage.platform.node.runtime.unit.Unit;

import static org.hage.platform.node.execution.phase.ExecutionPhaseType.MAIN__AGENTS_STEP;

@Slf4j
@SingletonComponent
public class AgentsStepPhase extends AbstractUnitPhase {

    @Override
    public ExecutionPhaseType getType() {
        return MAIN__AGENTS_STEP;
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::runAgents;
    }

}
