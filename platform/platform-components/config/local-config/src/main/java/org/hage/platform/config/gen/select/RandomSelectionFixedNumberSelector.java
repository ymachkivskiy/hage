package org.hage.platform.config.gen.select;

import com.google.common.base.Preconditions;
import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Set;

public class RandomSelectionFixedNumberSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        Preconditions.checkArgument(selectionData.getValue().isPresent(), "Fixed number random selector requires value");
        return chunk.getRandomPositions(selectionData.getValue().get());
    }

}
