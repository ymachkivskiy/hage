package org.hage.platform.node.runtime.unitmove;

import org.hage.platform.node.structure.Position;

public interface UnitConfigurationActivator {
    void activateConfigurationOnPosition(Position position, UnitConfiguration unitConfiguration);
}
