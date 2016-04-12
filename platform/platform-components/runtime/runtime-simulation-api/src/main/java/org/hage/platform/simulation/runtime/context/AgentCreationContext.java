package org.hage.platform.simulation.runtime.context;

import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Set;

public interface AgentCreationContext {
    /**
     * Returns all supported by this cell agent classes
     *
     * @return supported agent classes
     */
    Set<Class<? extends Agent>> getSupportedAgentsTypes();

    /**
     * Creates new agent instance in simulation call of given class if it is
     * supported by current simulation cell
     *
     * @param agentClazz class of agent to create
     * @param <T>   agent clazz
     * @throws UnsupportedAgentTypeException is thrown when given agent class is not supported by
     *                                       simulation cell controlled by current agent
     */
    <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException;

    /**
     * Creates new agent instance in simulation call of given class if it is
     * supported by current simulation cell
     *
     * @param agentClazz       class of agent to create
     * @param initializer initializer for newly created agent instance
     * @param <T>         agent agentClazz
     * @throws UnsupportedAgentTypeException is thrown when given agent class is not supported by
     *                                       simulation cell controlled by current agent
     */
    <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException;
}
