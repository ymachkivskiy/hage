package org.hage.platform.component.simulationconfig.load.generate.select;

import org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData;
import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.grid.Chunk;

import java.util.Set;

public interface PositionsSelector {
    Set<Position> select(Chunk chunk, PositionsSelectionData selectionData);
}
