package org.hage.platform.config.transl.count;

import org.hage.platform.config.def.agent.AgentCountData;

public interface AgentCountProvider {
    Integer getAgentCount(AgentCountData countData);
}
