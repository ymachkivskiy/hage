package org.hage.platform.component.runtime.unit.registry;

import org.hage.platform.component.runtime.unit.UnitPopulationLoader;
import org.hage.platform.component.structure.Position;

public interface PopulationLoaderRegistry {
    UnitPopulationLoader loaderForPosition(Position position);
}
