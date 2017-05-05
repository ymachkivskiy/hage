package org.hage.platform.node.runtime.unit.registry;

import org.hage.platform.node.runtime.unit.UnitPopulationLoader;
import org.hage.platform.node.structure.Position;

public interface PopulationLoaderRegistry {
    UnitPopulationLoader loaderForPosition(Position position);
}
