package org.hage.platform.simconf.load.definition;

import lombok.Data;
import org.hage.platform.node.structure.grid.Chunk;
import org.hage.platform.simconf.load.definition.agent.ChunkAgentDistribution;

import java.util.List;

@Data
public class ChunkPopulationQualifier {
    private final Chunk chunk;
    private final List<ChunkAgentDistribution> chunkAgentDistributions;
}
