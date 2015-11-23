package org.hage.platform.config.transl.select;

import org.hage.platform.config.def.agent.InternalPositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

class RandomSelectionFixedNumberSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, InternalPositionsSelectionData selectionData) {
        checkArgument(selectionData.getValue().isPresent(), "Fixed number random selector requires value");
        return chunk.getRandomPositions(selectionData.getValue().get());
    }

}
