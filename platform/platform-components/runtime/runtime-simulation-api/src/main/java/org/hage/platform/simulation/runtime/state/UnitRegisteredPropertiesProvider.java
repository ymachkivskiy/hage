package org.hage.platform.simulation.runtime.state;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import java.io.Serializable;
import java.util.List;

public interface UnitRegisteredPropertiesProvider extends Serializable {
    List<PropertyDescriptor> getRegisteredProperties();
}
