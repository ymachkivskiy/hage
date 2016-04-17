package org.hage.platform.component.runtime.unit.context;

import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

public interface AgentLocalEnvironment {
    String getUniqueIdentifier();

    AgentManageContext contextForAgent(AgentAdapter agentAdapter);

    ControlAgentManageContext contextForControlAgent();
}
