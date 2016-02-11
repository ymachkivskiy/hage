package org.hage.platform.config.load.adapter.generate;

import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier);
}
