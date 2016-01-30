package org.hage.platform.config.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

public interface AgentCountProvider {
    Integer getAgentCount(AgentCountData countData);
}
