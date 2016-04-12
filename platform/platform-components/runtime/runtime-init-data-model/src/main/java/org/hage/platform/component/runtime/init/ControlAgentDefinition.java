package org.hage.platform.component.runtime.init;

import lombok.Data;
import org.hage.platform.simulation.runtime.ControlAgent;

import java.io.Serializable;

@Data
public class ControlAgentDefinition implements Serializable{
    private final Class<? extends ControlAgent> clazz;
}
