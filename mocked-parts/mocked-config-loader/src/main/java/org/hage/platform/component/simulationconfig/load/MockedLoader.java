package org.hage.platform.component.simulationconfig.load;

import org.hage.mocked.simdata.MigrationCheckComponent;
import org.hage.mocked.simdata.SomeFooComponent;
import org.hage.mocked.simdata.StopConditionChecker;
import org.hage.mocked.simdata.agent.HeavyAgent;
import org.hage.mocked.simdata.agent.LightAgent;
import org.hage.mocked.simdata.agent.SimpleControlAgent;
import org.hage.mocked.simdata.state.ExamplePropertiesConfigurator;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.definition.ValueDefinition;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.ControlAgentDefinition;
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
import static org.hage.platform.component.loadbalance.config.BalanceMode.AFTER_STEP_COUNT;
import static org.hage.platform.component.rate.model.MeasurerType.CONCURRENCY;
import static org.hage.platform.component.rate.model.MeasurerType.RAM_MEMORY;
import static org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData.atMost;
import static org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.component.structure.Position.ZERO;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

class MockedLoader implements ConfigurationLoader {

    private static final SimulationOrganizationDefinition SIMULATION_DEFINITION = new SimulationOrganizationDefinition(
        new StructureDefinition(CLOSED, Dimensions.definedBy(2, 2, 4), VON_NEUMANN_NEGIHBORHOOD),
        asList(new AgentDefinition(HeavyAgent.class), new AgentDefinition(LightAgent.class)),
        new ControlAgentDefinition(SimpleControlAgent.class),
        singletonList(
            new ChunkPopulationQualifier(
                new Chunk(ZERO, Dimensions.definedBy(2, 2, 3)),
                singletonList(
                    new ChunkAgentDistribution(
                        new AgentDefinition(LightAgent.class),
                        atMost(2),
                        allPositions()
                    )
                )
            )
        )
    );


    @Override
    public InputConfiguration load() throws ConfigurationNotFoundException {
        ComponentDefinition globalComponent = new ComponentDefinition("globalComponent", SomeFooComponent.class, true);
        globalComponent.addConstructorArgument(new ValueDefinition(String.class, "GLOBAL"));

        return InputConfiguration
            .builder()
            .loadBalanceConfig(new LoadBalanceConfig(AFTER_STEP_COUNT, 1))
            .globalComponents(asList(
                globalComponent,
                new ComponentDefinition("migrationCheck", MigrationCheckComponent.class, true)
            ))
            .simulationDefinition(SIMULATION_DEFINITION)
            .computationRatingConfig(
                ComputationRatingConfig.builder()
                    .enabledRateMeasureTypes(EnumSet.of(RAM_MEMORY, CONCURRENCY))
                    .measurerRateConfigs(singletonList(
                        new MeasurerRateConfig(RAM_MEMORY, 5, 128)
                    )).build()
            )
            .stopConditionClazz(StopConditionChecker.class)
            .propertiesConfiguratorClazz(ExamplePropertiesConfigurator.class)
            .build();
    }


}
