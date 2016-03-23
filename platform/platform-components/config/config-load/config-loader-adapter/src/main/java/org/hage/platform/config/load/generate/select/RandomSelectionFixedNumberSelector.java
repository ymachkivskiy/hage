package org.hage.platform.config.load.generate.select;

import com.google.common.base.Preconditions;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.grid.Chunk;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class RandomSelectionFixedNumberSelector implements PositionsSelector {

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        Preconditions.checkArgument(selectionData.getValue().isPresent(), "Fixed number random selector requires value");
        return chunk.getRandomPositions(selectionData.getValue().get());
    }

}
