package org.hage.platform.component.simulation.structure.event;

import lombok.Builder;
import lombok.Data;
import org.hage.platform.component.simulation.structure.SimulationCell;

import java.util.List;

@Builder
@Data
public class SimulationStructureChangedEvent {
    private final List<SimulationCell> addedCells;
    private final List<SimulationCell> removedCells;
}
