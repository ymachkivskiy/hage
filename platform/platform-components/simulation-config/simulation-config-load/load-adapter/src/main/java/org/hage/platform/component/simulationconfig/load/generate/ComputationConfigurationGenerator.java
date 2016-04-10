package org.hage.platform.component.simulationconfig.load.generate;

import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.simulationconfig.Common;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.Specific;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.component.simulationconfig.load.definition.SimulationOrganizationDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.component.runtime.init.Population.emptyPopulation;


@HageComponent
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
