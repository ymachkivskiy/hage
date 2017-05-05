package org.hage.platform.node.runtime.activepopulation;

import org.hage.platform.node.runtime.unit.AgentMigrationTarget;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Collection;

public interface AgentsTargetEnvironment extends AgentMigrationTarget {
    void scheduleAddAgents(Collection<? extends Agent> agents);
}
