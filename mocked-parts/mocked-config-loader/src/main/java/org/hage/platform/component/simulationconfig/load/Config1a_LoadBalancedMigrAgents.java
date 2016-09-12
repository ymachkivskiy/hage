package org.hage.platform.component.simulationconfig.load;

import org.hage.mocked.simdata.Randomizer;
import org.hage.mocked.simdata.agent.MigratingAgent;
import org.hage.mocked.simdata.state.DenceProvokingPropertiesConfigurator;
import org.hage.mocked.simdata.stopcond.FixedSteps255;
import org.hage.platform.component.container.definition.ComponentDefinition;
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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hage.platform.component.rate.model.MeasurerType.CONCURRENCY;
import static org.hage.platform.component.rate.model.MeasurerType.RAM_MEMORY;
import static org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData.fixed;
import static org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.component.structure.Position.ZERO;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.FULL__TORUS;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

public class Config1a_LoadBalancedMigrAgents implements ConfigurationSupplier {

    private static final Dimensions gridDims = definedBy(15, 15, 15);
    public static final int BALANCE_STEPS_GAP = 50;


    @Override
    public String configurationName() {
        return "1aBalanced";
    }

    @Override
    public InputConfiguration getConfiguration() {

        return InputConfiguration.builder()

            .loadBalanceConfig(new LoadBalanceConfig(BalanceMode.AFTER_STEP_COUNT, BALANCE_STEPS_GAP))

            .globalComponents(asList(new ComponentDefinition("randomizer", Randomizer.class, true)))

            .simulationDefinition(new SimulationOrganizationDefinition(
                new StructureDefinition(FULL__TORUS, gridDims, VON_NEUMANN_NEGIHBORHOOD),
                singletonList(new AgentDefinition(MigratingAgent.class)),
                null,
                singletonList(
                    new ChunkPopulationQualifier(
                        new Chunk(ZERO, gridDims),
                        singletonList(
                            new ChunkAgentDistribution(new AgentDefinition(MigratingAgent.class),
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

            .stopConditionClazz(FixedSteps255.class)

            .build();
    }
}
