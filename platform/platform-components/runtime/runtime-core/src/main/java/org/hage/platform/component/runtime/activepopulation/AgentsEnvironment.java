package org.hage.platform.component.runtime.activepopulation;

import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.Collection;

public interface AgentsEnvironment {
    void setControlAgent(ControlAgent controlAgent);

    void addAgentsImmediately(Collection<? extends Agent> agents);

    void scheduleAddAgents(Collection<? extends Agent> agents);
}
