package org.hage.platform.component.runtime.event;

import lombok.Builder;
import lombok.Data;
import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.util.bus.Event;

import java.util.List;

// TODO: change naming
@Builder
@Data
public class SimulationStructureChangedEvent implements Event {
    private final List<ExecutionUnit> addedCells;
    private final List<ExecutionUnit> removedCells;
}
