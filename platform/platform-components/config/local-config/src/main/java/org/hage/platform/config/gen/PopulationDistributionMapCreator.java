package org.hage.platform.config.gen;

import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.config.def.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier);
}
