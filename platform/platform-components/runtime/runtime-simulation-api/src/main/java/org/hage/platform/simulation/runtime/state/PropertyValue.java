package org.hage.platform.simulation.runtime.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class PropertyValue implements Serializable{
    private final PropertyDescriptor descriptor;
    private final Object value;
}
