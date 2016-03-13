package org.hage.platform.config.load.definition.agent;

import lombok.Data;
import org.hage.platform.component.simulation.structure.definition.AgentDefinition;

@Data
public final class ChunkAgentDistribution {
    private final AgentDefinition agentDefinition;
    private final AgentCountData countData;
    private final PositionsSelectionData positionsSelectionData;
}
