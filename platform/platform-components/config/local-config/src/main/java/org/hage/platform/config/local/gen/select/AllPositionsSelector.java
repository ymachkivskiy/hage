package org.hage.platform.config.local.gen.select;

import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

public class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }

}
