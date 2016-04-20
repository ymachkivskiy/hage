package org.hage.platform.simulation.runtime.agent;

import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.hage.platform.simulation.runtime.context.StopConditionContext;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.List;
import java.util.Set;

public interface AgentManageContext extends AgentCreationContext, LocationContext, StopConditionContext {

    AgentAddress queryAddress();

    List<AgentAddress> queryOtherLocalAgents();

    List<AgentAddress> queryOtherLocalAgentsOfSameType();

    List<AgentAddress> queryOtherLocalAgentsOfType(Class<? extends Agent> agentClazz) throws UnsupportedAgentTypeException;

    void die();

}
