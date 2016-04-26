package org.hage.platform.simulation.runtime.control;

import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.hage.platform.simulation.runtime.context.StopConditionContext;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.Collection;
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

    void killAgent(AgentAddress agentAddress);

    List<AgentAddress> queryLocalAgentsAddresses();

    /**
     * Request for migration all local agents <b>except those, which will be created in current computation step</b> to
     * specified target unit. If migration succeeds, agents will be in target unit in next computation step.
     *
     * @param targetUnitAddress migration target unit address
     * @return true if targetUnitAddress is correct, is not address of current unit and migration is legal,
     * false otherwise.
     */
    boolean migrateAllAgentsTo(UnitAddress targetUnitAddress);

    /**
     * Request for migration of specified agents to target unit. If migration succeeds, agents will be in target unit in
     * next computation step.
     *
     * @param agentAddresses    addresses of agents which will participate in migration
     * @param targetUnitAddress target unit address
     * @return true if there is at least on correct local agent in agents collection, target unit address is corrent, is
     * not address of current unit and migration is legal, false otherwise.
     */
    boolean migrateAgentsTo(Collection<AgentAddress> agentAddresses, UnitAddress targetUnitAddress);

    /**
     * Request for migration of specified agent to target unit. If migration succeeds, agent will be in target unit in
     * next computation step.
     *
     * @param agentAddress      address of agent for migration
     * @param targetUnitAddress migration target unit address
     * @return true if agentAddress is correct address of local agent, targetUnitAddress is correct address of not current
     * unit and migration is legal, false otherwise.
     */
    boolean migrateAgentTo(AgentAddress agentAddress, UnitAddress targetUnitAddress);
}
