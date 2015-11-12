package org.hage.platform.config.def;

import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.List;

public interface AgentDestinationFilter {
    List<InternalPosition> chooseFrom(Chunk originalChunks);
}
