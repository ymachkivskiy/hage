package org.hage.platform.config.def;

import lombok.Data;
import org.hage.platform.config.def.agent.ChunkAgentDistribution;
import org.hage.platform.habitat.structure.Chunk;

import java.util.List;

@Data
public class ChunkPopulationQualifier {
    private final Chunk chunk;
    private final List<ChunkAgentDistribution> chunkAgentDistributions;
}
