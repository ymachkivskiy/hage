package org.hage.platform.config.load.generate;

import org.hage.platform.component.simulation.structure.definition.PopulationDistributionMap;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier);
}
