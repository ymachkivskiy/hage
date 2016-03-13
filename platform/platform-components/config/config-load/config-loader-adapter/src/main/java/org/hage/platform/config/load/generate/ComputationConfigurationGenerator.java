package org.hage.platform.config.load.generate;

import org.hage.platform.component.simulation.structure.SimulationOrganization;
import org.hage.platform.component.simulation.structure.definition.Population;
import org.hage.platform.config.Common;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.Specific;
import org.hage.platform.config.load.definition.HabitatOrganizationDefinition;
import org.hage.platform.config.load.definition.InputConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.component.simulation.structure.definition.Population.emptyDistributionMap;

@Component
public class ComputationConfigurationGenerator {

    @Autowired
    private PopulationDistributionMapCreator distributionMapCreator;

    public Configuration generate(InputConfiguration inputConfiguration) {
        return new Configuration(
            new Common(
                inputConfiguration.getComputationRatingConfig(),
                inputConfiguration.getGlobalComponents()
            ),
            new Specific(createSimulationOrganization(inputConfiguration.getHabitatConfiguration()))
        );
    }


    private SimulationOrganization createSimulationOrganization(HabitatOrganizationDefinition definition) {
        return new SimulationOrganization(
            definition.getStructureDefinition(),
            generateDistributionMap(definition)
        );
    }

    private Population generateDistributionMap(HabitatOrganizationDefinition source) {
        return source.getChunkPopulationQualifiers()
            .stream()
            .map(distributionMapCreator::createMap)
            .reduce(emptyDistributionMap(), Population::merge)
            ;
    }

}
