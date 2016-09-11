package org.hage.platform.component.simulationconfig.load;

import org.hage.mocked.simdata.agent.LightNotReproducibleNotMigratingAgent;
import org.hage.mocked.simdata.stopcond.FixedSteps1000;
import org.hage.platform.component.loadbalance.config.BalanceMode;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.simulationconfig.load.definition.ChunkPopulationQualifier;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.component.simulationconfig.load.definition.SimulationOrganizationDefinition;
import org.hage.platform.component.simulationconfig.load.definition.agent.ChunkAgentDistribution;
import org.hage.platform.component.structure.Position;
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
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.MOORE_NEIGHBORHOOD;

public class Config1b_LightNotRepNotMigrAgentsConfigSupplier implements ConfigurationSupplier {

    private static final Dimensions gridDims = definedBy(25, 25, 25);

    @Override
    public String configurationName() {
        return "1bLightA";
    }

    @Override
    public InputConfiguration getConfiguration() {

        return InputConfiguration.builder()

            .loadBalanceConfig(new LoadBalanceConfig(BalanceMode.DISABLED, 0))

            .simulationDefinition(new SimulationOrganizationDefinition(
                new StructureDefinition(CLOSED, gridDims, MOORE_NEIGHBORHOOD),
                singletonList(new AgentDefinition(LightNotReproducibleNotMigratingAgent.class)),
                null,
                singletonList(
                    new ChunkPopulationQualifier(
                        new Chunk(Position.position(0, 0, 0), gridDims),
                        singletonList(
                            new ChunkAgentDistribution(new AgentDefinition(LightNotReproducibleNotMigratingAgent.class),
                                fixed(50),
                                allPositions()
                            )
                        )
                    )
                )


            ))

            .computationRatingConfig(
                ComputationRatingConfig.builder()
                    .enabledRateMeasureTypes(EnumSet.of(RAM_MEMORY, CONCURRENCY))
                    .measurerRateConfigs(asList(
                        new MeasurerRateConfig(RAM_MEMORY, 5, 128),
                        new MeasurerRateConfig(CONCURRENCY, 2, 64)
                    )).build()
            )

            .stopConditionClazz(FixedSteps1000.class)

            .build();
    }
}
