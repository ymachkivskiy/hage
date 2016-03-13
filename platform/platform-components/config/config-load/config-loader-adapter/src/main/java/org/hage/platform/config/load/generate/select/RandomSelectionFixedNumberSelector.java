package org.hage.platform.config.load.generate.select;

import com.google.common.base.Preconditions;
import org.hage.platform.component.simulation.structure.definition.Chunk;
import org.hage.platform.component.simulation.structure.definition.InternalPosition;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class RandomSelectionFixedNumberSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        Preconditions.checkArgument(selectionData.getValue().isPresent(), "Fixed number random selector requires value");
        return chunk.getRandomPositions(selectionData.getValue().get());
    }

}
