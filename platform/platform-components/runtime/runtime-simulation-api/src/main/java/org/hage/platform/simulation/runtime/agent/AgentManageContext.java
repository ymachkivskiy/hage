package org.hage.platform.simulation.runtime.agent;

import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.hage.platform.simulation.runtime.context.StopConditionContext;

import java.util.Set;

public interface AgentManageContext extends AgentCreationContext, LocationContext, StopConditionContext {

    AgentAddress queryAddress();

    Set<AgentAddress> queryOtherLocalAgents();

    void die();

}
