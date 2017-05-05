package org.hage.platform.node.runtime.unit;

import org.hage.platform.node.runtime.activepopulation.AgentAdapter;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

public interface AgentExecutionContextEnvironment {
    String getUniqueIdentifier();

    AgentManageContext contextForAgent(AgentAdapter agentAdapter);

    ControlAgentManageContext contextForControlAgent();
}
