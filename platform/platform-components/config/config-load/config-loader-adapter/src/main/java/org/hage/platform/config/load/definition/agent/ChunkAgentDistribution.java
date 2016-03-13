package org.hage.platform.config.load.definition.agent;

import lombok.Data;
import org.hage.platform.component.simulation.agent.Agent;

@Data
public final class ChunkAgentDistribution {
    private final Agent agent;
    private final AgentCountData countData;
    private final PositionsSelectionData positionsSelectionData;
}
