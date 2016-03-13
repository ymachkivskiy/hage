package org.hage.platform.config.load.generate;

import org.hage.platform.component.simulation.structure.definition.Population;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    Population createMap(ChunkPopulationQualifier populationQualifier);
}
