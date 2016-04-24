package org.hage.platform.component.runtime.populationinit;

import org.hage.platform.component.structure.Position;

public interface PopulationLoaderRegistry {
    UnitPopulationLoader getPopulationLoaderFor(Position position);
}
