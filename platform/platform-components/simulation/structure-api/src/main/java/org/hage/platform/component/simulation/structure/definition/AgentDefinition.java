package org.hage.platform.component.simulation.structure.definition;

import lombok.Data;
import org.hage.platform.simulation.base.Agent;

@Data
public class AgentDefinition {
    private final Class<? extends Agent> clazz;
}
