package org.hage.platform.component.runtime.event;

import lombok.Data;
import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.util.bus.Event;

import java.util.List;

// TODO: change naming
@Data
public class SimulationStructureChangedEvent implements Event {
    private final List<? extends ExecutionUnit> addedUnits;
    private final List<? extends ExecutionUnit> removedUnits;
}
