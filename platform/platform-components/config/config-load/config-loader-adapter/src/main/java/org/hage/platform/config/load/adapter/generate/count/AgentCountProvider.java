package org.hage.platform.config.load.adapter.generate.count;

import org.hage.platform.config.load.definition.agent.AgentCountData;

public interface AgentCountProvider {
    Integer getAgentCount(AgentCountData countData);
}
