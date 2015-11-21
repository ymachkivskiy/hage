package org.hage.platform.config.transl;

import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.HabitatInternalConfiguration;
import org.hage.platform.config.def.PopulationDistributionMap;
import org.hage.platform.habitat.structure.StructureDefinition;


public class HabitatConfigurationTranslator {
    public HabitatInternalConfiguration toInternalModel(HabitatExternalConfiguration source) {

        StructureDefinition structureDefinition = source.getStructureDefinition();
        PopulationDistributionMap populationDistributionMap = getPopulationDistribution(source);

        return new HabitatInternalConfiguration(structureDefinition, populationDistributionMap);
    }

    private PopulationDistributionMap getPopulationDistribution(HabitatExternalConfiguration source) {
        return null;
    }
}
