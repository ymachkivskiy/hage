package org.hage.platform.component.runtime.definition;

import lombok.Data;
import org.hage.platform.simulation.runtime.Agent;

@Data
public class AgentDefinition {
    private final Class<? extends Agent> agentClass;
}
