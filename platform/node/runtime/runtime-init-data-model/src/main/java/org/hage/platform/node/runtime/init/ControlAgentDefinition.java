package org.hage.platform.node.runtime.init;

import lombok.Data;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.io.Serializable;

@Data
public class ControlAgentDefinition implements Serializable{
    private final Class<? extends ControlAgent> clazz;
}
