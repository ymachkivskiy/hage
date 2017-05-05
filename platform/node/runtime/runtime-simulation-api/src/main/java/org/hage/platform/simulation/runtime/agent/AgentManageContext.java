package org.hage.platform.simulation.runtime.agent;

import org.hage.platform.node.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.context.*;

import java.util.List;

public interface AgentManageContext extends AgentCreationContext, LocationContext, StopConditionContext, SimulationStateContext, UnitPropertiesQueryingContext {

    AgentAddress queryAddress();

    List<AgentAddress> queryOtherLocalAgents();

    List<AgentAddress> queryOtherLocalAgentsOfSameType();

    List<AgentAddress> queryOtherLocalAgentsOfType(Class<? extends Agent> agentClazz) throws UnsupportedAgentTypeException;

    /**
     * Request for agent death. Agent will not be available from next computation step.
     */
    void die();

    /**
     * Request for agent migration to specified unit. If migration succeed, agent will be in target unit in next computation step.
     *
     * @param targetUnitAddress migration target unit address
     * @return true if unit address is correct, is not address of current unit and migration is legal, false otherwise
     */
    boolean migrateTo(UnitAddress targetUnitAddress);

}
