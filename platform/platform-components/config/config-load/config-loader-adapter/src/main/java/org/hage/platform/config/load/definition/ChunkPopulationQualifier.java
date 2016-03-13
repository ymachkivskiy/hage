package org.hage.platform.config.load.definition;

import lombok.Data;
import org.hage.platform.component.simulation.structure.definition.Chunk;
import org.hage.platform.config.load.definition.agent.ChunkAgentDistribution;

import java.util.List;

@Data
public class ChunkPopulationQualifier {
    private final Chunk chunk;
    private final List<ChunkAgentDistribution> chunkAgentDistributions;
}
