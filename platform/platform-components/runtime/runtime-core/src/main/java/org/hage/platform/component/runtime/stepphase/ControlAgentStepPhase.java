package org.hage.platform.component.runtime.stepphase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.AgentsExecutionUnit;

@Slf4j
@SingletonComponent
public class ControlAgentStepPhase extends AbstractAgentUnitPhase {

    @Override
    public String getPhaseName() {
        return "Control agent step";
    }

    @Override
    protected Runnable extractRunnable(AgentsExecutionUnit agentsExecutionUnit) {
        return agentsExecutionUnit.getAgentsRunner()::runControlAgent;
    }

}
