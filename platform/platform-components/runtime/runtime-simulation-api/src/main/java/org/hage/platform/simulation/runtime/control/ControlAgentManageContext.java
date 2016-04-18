package org.hage.platform.simulation.runtime.control;

import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.hage.platform.simulation.runtime.context.StopConditionContext;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.List;

public interface ControlAgentManageContext extends AgentCreationContext, LocationContext, StopConditionContext {

    /**
     * Returns all agents of given agentClazz located in controlled simulation cell.
     *
     * @param agentClazz class of agents
     * @param <T>        agent type
     * @return list of agents instances of given class with address
     * @throws UnsupportedAgentTypeException is thrown when given agent class is not supported by
     *                                       simulation cell controlled by current agent
     */
    <T extends Agent> List<AddressedAgent<T>> queryAgentsOfType(Class<T> agentClazz) throws UnsupportedAgentTypeException;

    boolean killAgent(AgentAddress agentAddress);

    List<AgentAddress> queryLocalAgentsAddresses();

}
