package org.hage.platform.config.load.generate.select;

import org.hage.platform.component.simulation.structure.definition.Chunk;
import org.hage.platform.component.simulation.structure.definition.InternalPosition;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public class AllPositionsSelector implements PositionsSelector {

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        return chunk.getInternalPositions();
    }

}
