package org.hage.platform.component.runtime.unitmove;

import org.hage.platform.component.structure.Position;

public interface UnitConfigurationActivator {
    void activateConfigurationOnPosition(Position position, UnitConfiguration unitConfiguration);
}
