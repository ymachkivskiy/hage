package org.hage.platform.simconf.load.definition.agent;

import lombok.Data;
import org.hage.platform.node.runtime.init.AgentDefinition;

@Data
public final class ChunkAgentDistribution {
    private final AgentDefinition agentDefinition;
    private final AgentCountData countData;
    private final PositionsSelectionData positionsSelectionData;
}
