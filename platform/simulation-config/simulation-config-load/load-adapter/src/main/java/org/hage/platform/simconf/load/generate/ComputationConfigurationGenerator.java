package org.hage.platform.simconf.load.generate;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.init.ContainerConfiguration;
import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.simconf.Common;
import org.hage.platform.simconf.Configuration;
import org.hage.platform.simconf.Specific;
import org.hage.platform.simconf.load.definition.InputConfiguration;
import org.hage.platform.simconf.load.definition.SimulationOrganizationDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.node.runtime.init.Population.emptyPopulation;


@SingletonComponent
public class ComputationConfigurationGenerator {

    @Autowired
    private PopulationDistributionMapCreator distributionMapCreator;

    public Configuration generate(InputConfiguration inConf) {
        return new Configuration(
            new Common(
                inConf.getLoadBalanceConfig(),
                new ContainerConfiguration(
                    inConf.getSimulationDefinition().getAgentDefinitions(),
                    inConf.getSimulationDefinition().getControlAgentDefinition(),
                    inConf.getGlobalComponents()
                ),
                inConf.getSimulationDefinition().getStructureDefinition(),
                inConf.getStopConditionClazz(),
                inConf.getPropertiesConfiguratorClazz()
            ),
            new Specific(generateDistributionMap(inConf.getSimulationDefinition()))
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
