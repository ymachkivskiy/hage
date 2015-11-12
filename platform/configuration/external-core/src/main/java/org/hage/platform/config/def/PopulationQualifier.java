package org.hage.platform.config.def;

import lombok.Builder;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.Chunk;

@Builder
public final class PopulationQualifier {
    private final AgentDefinition agent;
    private final AgentCountSupplier agentCountSupplier;
    private final AgentDestinationFilter agentDestinationFilter;
    private final Chunk populationDestination;
}
