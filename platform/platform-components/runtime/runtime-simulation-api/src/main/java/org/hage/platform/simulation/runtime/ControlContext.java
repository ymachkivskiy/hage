package org.hage.platform.simulation.runtime;

import java.util.List;
import java.util.Set;

public interface ControlContext extends CommonContext {

    /**
     * Returns all supported by this cell agent classes
     * @return supported agent classes
     */
    Set<Class<? extends Agent>> getSupportedAgentsTypes();

    /**
     * Returns all agents of given clazz located in controlled simulation cell.
     *
     * @param clazz class of agents
     * @param <T> agent type
     * @return list of agents instances of given class
     * @throws UnsupportedAgentTypeException is thrown when given agent class is not supported by
     *   simulation cell controlled by current agent
     */
    <T extends Agent> List<T> queryAgentsOfType(Class<T> clazz) throws UnsupportedAgentTypeException;

    /**
     *  Creates new agent instance in simulation call of given class if it is
     *  supported by current simulation cell
     *
     * @param clazz class of agent to create
     * @param <T> agent clazz
     * @return newly created agent instance
     * @throws UnsupportedAgentTypeException is thrown when given agent class is not supported by
     *    simulation cell controlled by current agent
     */
    <T extends Agent> T newAgent(Class<T> clazz) throws UnsupportedAgentTypeException;
}
