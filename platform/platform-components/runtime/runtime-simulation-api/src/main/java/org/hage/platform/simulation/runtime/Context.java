package org.hage.platform.simulation.runtime;

import java.util.Set;

public interface Context extends CommonContext {

    String getAgentFriendlyName();

    AgentAddress queryAddress();

    Set<AgentAddress> queryLocalAgents();

}
