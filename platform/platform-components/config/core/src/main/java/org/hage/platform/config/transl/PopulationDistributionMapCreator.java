package org.hage.platform.config.transl;

import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.PopulationDistributionMap;

public interface PopulationDistributionMapCreator {
    PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier);
}
