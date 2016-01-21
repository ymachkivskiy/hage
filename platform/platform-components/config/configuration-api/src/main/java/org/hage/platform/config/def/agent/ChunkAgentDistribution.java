package org.hage.platform.config.def.agent;

import lombok.Data;
import org.hage.platform.habitat.AgentDefinition;

@Data
public final class ChunkAgentDistribution {
    private final AgentDefinition agent;
    private final AgentCountData countData;
    private final PositionsSelectionData positionsSelectionData;
}
