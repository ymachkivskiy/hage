package org.hage.platform.component.simulationconfig.load.generate.count;

import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;

public interface AgentCountProvider {
    Integer getAgentCount(AgentCountData countData);
}
