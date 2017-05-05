package org.hage.platform.component.simulationconfig.load.generate;

import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.component.simulationconfig.load.definition.ChunkPopulationQualifier;

public interface PopulationDistributionMapCreator {
    Population createMap(ChunkPopulationQualifier populationQualifier);
}
