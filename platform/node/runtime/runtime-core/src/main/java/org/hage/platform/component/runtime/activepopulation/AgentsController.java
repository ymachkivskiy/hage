package org.hage.platform.component.runtime.activepopulation;

import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Collection;
import java.util.List;

public interface AgentsController {
    <T extends Agent> List<AgentAdapter> getAdaptersForAgentsOfType(Class<T> agentClazz);

    void scheduleRemoveWithKilling(AgentAdapter agentAdapter);

    void scheduleRemove(Collection<AgentAdapter> agentAdapters);

    void scheduleRemoveAll();

    boolean isLocalAgentAdapter(AgentAdapter agentAdapter);

    Collection<AgentAdapter> getAllAdapters();

}
