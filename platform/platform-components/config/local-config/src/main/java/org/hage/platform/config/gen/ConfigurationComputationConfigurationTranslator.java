package org.hage.platform.config.gen;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.HabitatGeography;
import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.config.def.HabitatOrganizationDefinition;
import org.hage.platform.config.provider.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationComputationConfigurationTranslator {

    @Autowired
    private PopulationDistributionMapCreator distributionMapCreator;

    public ComputationConfiguration translate(Configuration configuration) {
        return ComputationConfiguration.builder()
                .ratingConfig(configuration.getComputationRatingConfig())
                .globalComponents(configuration.getGlobalComponents())
                .habitatGeography(getGeographyForOrganizationDefinition(configuration.getHabitatConfiguration()))
                .build();
    }


    private HabitatGeography getGeographyForOrganizationDefinition(HabitatOrganizationDefinition definition) {
        return new HabitatGeography(
                definition.getStructureDefinition(),
                generateDistributionMap(definition)
        );
    }

    private PopulationDistributionMap generateDistributionMap(HabitatOrganizationDefinition source) {
        return source.getChunkPopulationQualifiers()
                .stream()
                .map(distributionMapCreator::createMap)
                .reduce(PopulationDistributionMap.emptyDistributionMap(), PopulationDistributionMap::merge)
                ;
    }

}
