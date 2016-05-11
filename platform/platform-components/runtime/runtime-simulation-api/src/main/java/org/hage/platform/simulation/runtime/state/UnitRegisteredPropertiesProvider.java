package org.hage.platform.simulation.runtime.state;

import java.io.Serializable;
import java.util.List;

public interface UnitRegisteredPropertiesProvider extends Serializable {
    List<PropertyDescriptor> getRegisteredProperties();
}
