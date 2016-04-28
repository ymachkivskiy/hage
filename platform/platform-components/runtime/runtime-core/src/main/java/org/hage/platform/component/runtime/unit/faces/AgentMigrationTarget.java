package org.hage.platform.component.runtime.unit.faces;

import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Collection;

public interface AgentMigrationTarget {
    void addAgentsImmediately(Collection<? extends Agent> agents);
}
