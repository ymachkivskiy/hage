package org.hage.platform.component.runtime.stateprops;

import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.structure.Position;

public interface UnitPropertiesControllerFactory {
    UnitPropertiesController createUnitPropertiesController(Position position, UnitComponentCreationController componentCreationController);
}
