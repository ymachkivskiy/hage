package org.hage.platform.node.runtime.unit;

import org.hage.platform.node.runtime.activepopulation.AgentsController;
import org.hage.platform.node.runtime.container.AgentsCreator;
import org.hage.platform.node.runtime.location.UnitLocationController;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesProvider;

public interface AgentContextAdapterFactory {
    AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController, UnitPropertiesProvider localUnitPropertiesProvider);
}
