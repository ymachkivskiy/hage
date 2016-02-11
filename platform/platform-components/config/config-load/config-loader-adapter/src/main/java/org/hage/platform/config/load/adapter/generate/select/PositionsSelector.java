package org.hage.platform.config.load.adapter.generate.select;

import org.hage.platform.config.load.definition.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

public interface PositionsSelector {
    Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData);
}
