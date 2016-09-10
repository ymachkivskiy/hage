package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesProvider;

public interface AgentContextAdapterFactory {
    AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController, UnitPropertiesProvider localUnitPropertiesProvider);
}
