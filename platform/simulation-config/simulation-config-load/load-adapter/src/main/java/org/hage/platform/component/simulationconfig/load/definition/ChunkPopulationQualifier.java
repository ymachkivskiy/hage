package org.hage.platform.component.simulationconfig.load.definition;

import lombok.Data;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.simulationconfig.load.definition.agent.ChunkAgentDistribution;

import java.util.List;

@Data
public class ChunkPopulationQualifier {
    private final Chunk chunk;
    private final List<ChunkAgentDistribution> chunkAgentDistributions;
}
