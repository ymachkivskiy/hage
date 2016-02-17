package org.hage.platform.component.simulation.structure.event;

import lombok.Builder;
import lombok.Data;
import org.hage.platform.component.simulation.structure.SimulationCell;

import java.util.Collection;

@Builder
@Data
public class SimulationStructureChangedEvent {
    private final Collection<SimulationCell> addedCells;
    private final Collection<SimulationCell> removedCells;
}
