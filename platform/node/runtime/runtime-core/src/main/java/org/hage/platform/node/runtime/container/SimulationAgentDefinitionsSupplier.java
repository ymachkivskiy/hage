package org.hage.platform.node.runtime.container;

import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.Optional;
import java.util.Set;

public interface SimulationAgentDefinitionsSupplier {

    boolean isSupportedAgent(Class<? extends Agent> agentClazz);

    Set<Class<? extends Agent>> getSupportedAgentTypes();

    Optional<Class<? extends ControlAgent>> getControlAgentType();
}
