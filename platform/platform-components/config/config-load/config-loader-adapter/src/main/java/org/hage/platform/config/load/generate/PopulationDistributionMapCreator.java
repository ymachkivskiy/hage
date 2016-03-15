package org.hage.platform.config.load.generate;

import org.hage.platform.component.runtime.definition.Population;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    Population createMap(ChunkPopulationQualifier populationQualifier);
}
