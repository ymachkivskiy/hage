package org.hage.platform.config.transl.select;

import org.hage.platform.config.def.agent.InternalPositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

public interface PositionsSelector {
    Set<InternalPosition> select(Chunk chunk, InternalPositionsSelectionData selectionData);
}
