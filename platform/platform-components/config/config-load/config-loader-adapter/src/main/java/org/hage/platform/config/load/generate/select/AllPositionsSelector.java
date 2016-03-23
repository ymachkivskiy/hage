package org.hage.platform.config.load.generate.select;

import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.component.structure.connections.grid.Chunk;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }

}
