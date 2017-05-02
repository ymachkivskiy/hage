package org.hage.platform.simulation.runtime.state.property;

import lombok.Data;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import java.io.Serializable;

@Data
public class PropertyValue implements Serializable {
    private final PropertyDescriptor descriptor;
    private final Object value;
}
