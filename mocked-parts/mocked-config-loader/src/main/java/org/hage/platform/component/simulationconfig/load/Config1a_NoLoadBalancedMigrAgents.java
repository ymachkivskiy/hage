package org.hage.platform.component.simulationconfig.load;

import org.hage.mocked.simdata.agent.LightReproducibleNotMigratingAgent;
import org.hage.mocked.simdata.state.DenceProvokingPropertiesConfigurator;
import org.hage.mocked.simdata.stopcond.FixedSteps100;
import org.hage.platform.component.loadbalance.config.BalanceMode;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.simulationconfig.load.definition.ChunkPopulationQualifier;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.component.simulationconfig.load.definition.SimulationOrganizationDefinition;
import org.hage.platform.component.simulationconfig.load.definition.agent.ChunkAgentDistribution;
import org.hage.platform.component.structure.StructureDefinition;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.structure.grid.Dimensions;

import java.util.EnumSet;

import static java.util.Collections.singletonList;
import static org.hage.platform.component.rate.model.MeasurerType.CONCURRENCY;
import static org.hage.platform.component.rate.model.MeasurerType.RAM_MEMORY;
import static org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData.fixed;
import static org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.component.structure.Position.ZERO;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.FULL__TORUS;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

public class Config1a_NoLoadBalancedMigrAgents implements ConfigurationSupplier {

    private static final Dimensions gridDims = definedBy(10, 10, 10);

    @Override
    public String configurationName() {
        return "1aNotBalanced";
    }

    @Override
    public InputConfiguration getConfiguration() {

        return InputConfiguration.builder()

            .loadBalanceConfig(new LoadBalanceConfig(BalanceMode.DISABLED, 0))

            .simulationDefinition(new SimulationOrganizationDefinition(
                new StructureDefinition(FULL__TORUS, gridDims, VON_NEUMANN_NEGIHBORHOOD),
                singletonList(new AgentDefinition(LightReproducibleNotMigratingAgent.class)),
                null,
                singletonList(
                    new ChunkPopulationQualifier(
                        new Chunk(ZERO, gridDims),
                        singletonList(
                            new ChunkAgentDistribution(new AgentDefinition(LightReproducibleNotMigratingAgent.class),
                                fixed(5),
                                allPositions()
                            )
                        )
                    )
                )


            ))

            .computationRatingConfig(
                ComputationRatingConfig.builder()
                    .enabledRateMeasureTypes(EnumSet.of(RAM_MEMORY, CONCURRENCY))
                    .measurerRateConfigs(singletonList(
                        new MeasurerRateConfig(RAM_MEMORY, 5, 128)
                    )).build()
            )

            .propertiesConfiguratorClazz(DenceProvokingPropertiesConfigurator.class)

            .stopConditionClazz(FixedSteps100.class)

            .build();
    }
}
