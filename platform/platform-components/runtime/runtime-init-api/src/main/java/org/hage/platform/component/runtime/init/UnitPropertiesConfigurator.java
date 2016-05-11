package org.hage.platform.component.runtime.init;

import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;

public interface UnitPropertiesConfigurator {
    void configureWith(Class<? extends UnitPropertiesStateComponent> unitPropertiesStateComponentClazz);
}
