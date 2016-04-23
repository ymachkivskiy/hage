package org.hage.platform.component.runtime.stepphase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.api.AgentsExecutionUnit;

@Slf4j
@SingletonComponent
public class AgentsStepPhase extends AbstractAgentUnitPhase {

    @Override
    public String getPhaseName() {
        return "Agents step";
    }

    @Override
    protected Runnable extractRunnable(AgentsExecutionUnit agentsExecutionUnit) {
        return agentsExecutionUnit.getAgentsRunner()::runAgents;
    }

}
