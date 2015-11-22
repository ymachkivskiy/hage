package org.hage.platform.config.transl;

import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.PopulationDistributionMap;

class PopulationDistributionMapCreatorImpl implements PopulationDistributionMapCreator{
    @Override
    public PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier) {
        return PopulationDistributionMap.empty();
    }
}
