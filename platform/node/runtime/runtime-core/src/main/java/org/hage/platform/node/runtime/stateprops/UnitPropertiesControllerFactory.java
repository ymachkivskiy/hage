package org.hage.platform.node.runtime.stateprops;

import org.hage.platform.node.structure.Position;

public interface UnitPropertiesControllerFactory {
    UnitPropertiesController createUnitPropertiesController(Position position);

    UnitPropertiesController createPropertiesControllerWithInitialState(Position position, PropertiesControllerInitialState initialState);
}
