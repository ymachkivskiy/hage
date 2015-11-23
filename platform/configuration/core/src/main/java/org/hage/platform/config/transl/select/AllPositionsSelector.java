package org.hage.platform.config.transl.select;

import org.hage.platform.config.def.agent.InternalPositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

import static java.util.Collections.emptySet;

class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, InternalPositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }


}
