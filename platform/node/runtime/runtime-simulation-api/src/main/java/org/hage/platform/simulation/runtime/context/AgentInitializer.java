package org.hage.platform.simulation.runtime.context;

import org.hage.platform.simulation.runtime.agent.Agent;

public interface AgentInitializer<T extends Agent> {
    void initAgent(T agent);
}
