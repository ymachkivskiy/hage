package org.hage.platform.component.runtime.unit.context;

import org.hage.platform.component.runtime.activepopulation.AgentAdapter;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

public interface AgentExecutionContextEnvironment {
    String getUniqueIdentifier();

    AgentManageContext contextForAgent(AgentAdapter agentAdapter);

    ControlAgentManageContext contextForControlAgent();
}
