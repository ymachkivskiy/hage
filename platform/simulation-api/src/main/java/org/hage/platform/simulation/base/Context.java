package org.hage.platform.simulation.base;

import org.hage.platform.simulation.identification.AgentAddress;

import java.util.Set;

public interface Context extends CommonContext {

    String getAgentFriendlyName();

    AgentAddress queryAddress();

    Set<AgentAddress> queryLocalAgents();

}
