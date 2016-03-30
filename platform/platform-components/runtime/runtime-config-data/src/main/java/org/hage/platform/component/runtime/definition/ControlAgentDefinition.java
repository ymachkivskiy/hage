package org.hage.platform.component.runtime.definition;

import lombok.Data;
import org.hage.platform.simulation.runtime.ControlAgent;

@Data
public class ControlAgentDefinition {
    private final Class<? extends ControlAgent> clazz;
}
