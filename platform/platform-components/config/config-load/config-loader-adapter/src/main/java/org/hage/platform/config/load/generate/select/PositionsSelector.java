package org.hage.platform.config.load.generate.select;

import org.hage.platform.component.simulation.structure.definition.Chunk;
import org.hage.platform.component.simulation.structure.definition.Position;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Set;

public interface PositionsSelector {
    Set<Position> select(Chunk chunk, PositionsSelectionData selectionData);
}
