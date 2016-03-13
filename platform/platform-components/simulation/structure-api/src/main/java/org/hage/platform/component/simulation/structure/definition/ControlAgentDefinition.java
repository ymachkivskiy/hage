package org.hage.platform.component.simulation.structure.definition;

import lombok.Data;
import org.hage.platform.simulation.base.ControlAgent;

@Data
public class ControlAgentDefinition {
    private final Class<? extends ControlAgent> clazz;
}
