package org.hage.platform.simconf.load.generate;

import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.simconf.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    Population createMap(ChunkPopulationQualifier populationQualifier);
}
