package org.hage.platform.config.load.generate.select;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }

}
