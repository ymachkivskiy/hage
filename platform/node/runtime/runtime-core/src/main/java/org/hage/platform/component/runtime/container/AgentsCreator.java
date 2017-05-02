package org.hage.platform.component.runtime.container;

import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.Set;

public interface AgentsCreator {
    <T extends Agent> void checkAgentClazz(Class<T> agentClazz) throws UnsupportedAgentTypeException;

    Set<Class<? extends Agent>> getSupportedAgentsTypes();

    <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber, boolean addImmediately) throws UnsupportedAgentTypeException;
}
