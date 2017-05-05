package org.hage.platform.simconf.load.generate.select;

import com.google.common.base.Preconditions;
import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.grid.Chunk;
import org.hage.platform.simconf.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class RandomSelectionFixedNumberSelector implements PositionsSelector {

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        Preconditions.checkArgument(selectionData.getValue().isPresent(), "Fixed number random selector requires value");
        return chunk.getRandomPositions(selectionData.getValue().get());
    }

}
