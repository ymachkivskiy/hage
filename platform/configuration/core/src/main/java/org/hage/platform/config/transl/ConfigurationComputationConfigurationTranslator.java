package org.hage.platform.config.transl;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.HabitatInternalConfiguration;
import org.hage.platform.config.def.PopulationDistributionMap;
import org.hage.platform.config.loader.Configuration;
import org.hage.platform.habitat.structure.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class ConfigurationComputationConfigurationTranslator {

    private PopulationDistributionMapCreator distributionMapCreator;


    public ComputationConfiguration translate(Configuration configuration) {
        return ComputationConfiguration.builder()
            .localComponents(configuration.getLocalComponents())
            .globalComponents(configuration.getGlobalComponents())
            .habitatConfiguration(translateHabitatConfiguration(configuration.getHabitatConfiguration()))
            .build();
    }


    private HabitatInternalConfiguration translateHabitatConfiguration(HabitatExternalConfiguration source) {

        StructureDefinition structureDefinition = source.getStructureDefinition();
        PopulationDistributionMap populationDistributionMap = getPopulationDistribution(source);

        return new HabitatInternalConfiguration(structureDefinition, populationDistributionMap);
    }

    private PopulationDistributionMap getPopulationDistribution(HabitatExternalConfiguration source) {
        return source.getChunkPopulationQualifiers()
            .stream()
            .map(distributionMapCreator::createMap)
            .reduce(PopulationDistributionMap.empty(), PopulationDistributionMap::merge)
            ;
    }

    @Required
    public void setDistributionMapCreator(PopulationDistributionMapCreator distributionMapCreator) {
        this.distributionMapCreator = distributionMapCreator;
    }
}
