package org.hage.platform.component.runtime.activepopulation;

import org.hage.platform.component.runtime.unit.AgentMigrationTarget;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.Collection;

public interface AgentsTargetEnvironment extends AgentMigrationTarget {
    void setControlAgent(ControlAgent controlAgent);

    void scheduleAddAgents(Collection<? extends Agent> agents);
}
