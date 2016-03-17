package org.hage.platform.config.load.generate;

import org.hage.platform.component.runtime.definition.Population;
import org.hage.platform.config.Common;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.Specific;
import org.hage.platform.config.load.definition.InputConfiguration;
import org.hage.platform.config.load.definition.SimulationOrganizationDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.component.runtime.definition.Population.emptyPopulation;


@Component
public class ComputationConfigurationGenerator {

    @Autowired
    private PopulationDistributionMapCreator distributionMapCreator;

    public Configuration generate(InputConfiguration inputConfiguration) {
        return new Configuration(
            new Common(
                inputConfiguration.getComputationRatingConfig(),
                inputConfiguration.getGlobalComponents(),
                inputConfiguration.getHabitatConfiguration().getStructureDefinition()
            ),
            new Specific(generateDistributionMap(inputConfiguration.getHabitatConfiguration()))
        );
    }

    private Population generateDistributionMap(SimulationOrganizationDefinition source) {
        return source.getChunkPopulationQualifiers()
            .stream()
            .map(distributionMapCreator::createMap)
            .reduce(emptyPopulation(), Population::merge)
            ;
    }

}
