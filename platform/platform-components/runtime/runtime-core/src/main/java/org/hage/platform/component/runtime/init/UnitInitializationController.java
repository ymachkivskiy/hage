package org.hage.platform.component.runtime.init;

import org.hage.platform.component.runtime.init.UnitPopulationInitializer;
import org.hage.platform.component.structure.Position;

public interface UnitInitializationController {
    UnitPopulationInitializer getInitializerForUnitOnPosition(Position position);
}
