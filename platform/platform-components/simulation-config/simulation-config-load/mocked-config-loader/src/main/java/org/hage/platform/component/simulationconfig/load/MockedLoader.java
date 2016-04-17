package org.hage.platform.component.simulationconfig.load;

import org.hage.example.SomeFooComponent;
import org.hage.example.agent.HeavyAgent;
import org.hage.example.agent.LightAgent;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.definition.ValueDefinition;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.rate.model.MeasurerType;
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
import static org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData.fixed;
import static org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.component.structure.Position.ZERO;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

class MockedLoader implements ConfigurationLoader {

    private static final SimulationOrganizationDefinition SIMULATION_DEFINITION = new SimulationOrganizationDefinition(
        new StructureDefinition(CLOSED, Dimensions.definedBy(2, 2, 4), VON_NEUMANN_NEGIHBORHOOD),
        asList(new AgentDefinition(HeavyAgent.class), new AgentDefinition(LightAgent.class)),
        null, // TODO: add control agent
        singletonList(
            new ChunkPopulationQualifier(
                new Chunk(ZERO, Dimensions.definedBy(2, 2, 3)),
                asList(
                    new ChunkAgentDistribution(
                        new AgentDefinition(LightAgent.class),
                        fixed(2),
                        allPositions()
                    )
//                    ,
//                    new ChunkAgentDistribution(
//                        new AgentDefinition(HeavyAgent.class),
//                        atMost(2),
//                        randomPositions(2)
//                    )
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
            .globalComponents(singletonList(globalComponent))
            .simulationDefinition(SIMULATION_DEFINITION)
            .computationRatingConfig(
                ComputationRatingConfig.builder()
                    .enabledRateMeasureTypes(EnumSet.of(MeasurerType.RAM_MEMORY))
                    .measurerRateConfigs(singletonList(
                        new MeasurerRateConfig(MeasurerType.RAM_MEMORY, 2, 5)
                    )).build()
            )
            .build();
    }


}
