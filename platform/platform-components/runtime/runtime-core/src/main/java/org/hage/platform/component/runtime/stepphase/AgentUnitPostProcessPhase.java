package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.api.AgentsExecutionUnit;

@SingletonComponent
public class AgentUnitPostProcessPhase extends AbstractAgentUnitPhase {

    @Override
    public String getPhaseName() {
        return "Agent unit post process";
    }


    @Override
    protected Runnable extractRunnable(AgentsExecutionUnit agentsExecutionUnit) {
        return agentsExecutionUnit::performPostProcessing;
    }
}
