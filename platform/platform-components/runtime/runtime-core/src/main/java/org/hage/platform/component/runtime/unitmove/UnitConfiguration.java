package org.hage.platform.component.runtime.unitmove;

import lombok.Data;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;

import java.io.Serializable;

@Data
public class UnitConfiguration implements Serializable {
    private final ControlAgent controlAgent;
    private final UnitPropertiesUpdater propertiesUpdater;
}
