package org.hage.platform.component.simulationconfig.load.generate.select;

import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.grid.Chunk;
import org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }

}
