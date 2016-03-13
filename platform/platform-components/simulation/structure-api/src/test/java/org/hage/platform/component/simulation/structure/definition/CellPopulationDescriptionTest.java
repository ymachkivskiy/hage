package org.hage.platform.component.simulation.structure.definition;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.component.simulation.agent.Agent;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CellPopulationDescriptionTest {

    @Test
    public void shouldMergeCellPopulationDescriptionWithNonEmptyDescription() throws Exception {

        // given

        final Agent firstAgent = mock(Agent.class);
        final Integer firstAgentCount = 13;
        final Agent secondAgent = mock(Agent.class);
        final Integer secondAgentCount = 110;

        final CellPopulationDescription firstDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                firstAgent, firstAgentCount,
                secondAgent, secondAgentCount
            )
        );

        final Agent thirdAgent = mock(Agent.class);
        final Integer thirdAgentCount = 17;

        final CellPopulationDescription secondDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                thirdAgent, thirdAgentCount
            )
        );

        // when

        CellPopulationDescription mergedDef = firstDescr.merge(secondDescr);

        // then

        assertThat(mergedDef.getAgents()).containsOnly(firstAgent, secondAgent, thirdAgent);
        assertThat(mergedDef.getAgentsCount()).isEqualTo(firstAgentCount + secondAgentCount + thirdAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(firstAgent)).isEqualTo(firstAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(secondAgent)).isEqualTo(secondAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(thirdAgent)).isEqualTo(thirdAgentCount);


    }

    @Test
    public void shouldMergeCellPopulationDescriptionsWithSameAgents() throws Exception {

        // given

        final Agent commonDef = mock(Agent.class);
        final Agent firstAgentDef = mock(Agent.class);

        final Integer firstCommonAgentCount = 110;
        final CellPopulationDescription firstDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                firstAgentDef, 13,
                commonDef, firstCommonAgentCount
            )
        );

        final Integer secondCommonAgentCount = 17;
        final CellPopulationDescription secondDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                commonDef, secondCommonAgentCount
            )
        );

        // when

        CellPopulationDescription mergedDef = firstDescr.merge(secondDescr);

        // then


        assertThat(mergedDef.getAgentCountForDefinition(commonDef)).isEqualTo(firstCommonAgentCount + secondCommonAgentCount);
        assertThat(mergedDef.getAgents()).containsOnly(firstAgentDef, commonDef);
    }

    @Test
    public void shouldMergeCellPopulationDescriptionWithEmptyCellPopulationDescription() throws Exception {

        // given

        final Agent agentDefinition = mock(Agent.class);
        final Integer agentCount = 13;

        final CellPopulationDescription firstDef = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulationDescription mergedDef = firstDef.merge(CellPopulationDescription.emptyPopulation());

        // then

        assertThat(mergedDef.getAgents()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);

    }

    @Test
    public void shouldMergeEmptyPopulationDescriptionWithCellPopulationDescription() throws Exception {

        // given

        final Agent agentDefinition = mock(Agent.class);
        final Integer agentCount = 13;

        final CellPopulationDescription firstDef = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulationDescription mergedDef = CellPopulationDescription.emptyPopulation().merge(firstDef);

        // then

        assertThat(mergedDef.getAgents()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);


    }

    @Test
    public void shouldContainNoAgents() throws Exception {

        // given

        final CellPopulationDescription populationDescription = CellPopulationDescription.emptyPopulation();

        // when

        // then

        assertThat(populationDescription.getAgents()).isEmpty();

    }

    @Test
    public void shouldContainAgents() throws Exception {

        // given

        final Agent agentDefinition = mock(Agent.class);

        final CellPopulationDescription description = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, 13)
        );

        // when


        // then

        assertThat(description.getAgents()).containsExactly(agentDefinition);


    }

    @Test
    public void shouldReturnZeroAgentCountForMissingDefinition() throws Exception {

        // given

        final CellPopulationDescription description = CellPopulationDescription.emptyPopulation();

        // when

        // then

        assertThat(description.getAgentCountForDefinition(mock(Agent.class))).isZero();


    }

    @Test
    public void shouldReturnAgentCountForDefinition() throws Exception {

        // given

        final Agent agentDefinition = mock(Agent.class);
        final int agentsCount = 13;

        final CellPopulationDescription description = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, agentsCount)
        );

        // when


        // then

        assertThat(description.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentsCount);


    }

    @Test
    public void shouldReturnAgentsCount() throws Exception {

        // given

        final int firstAgentsCount = 13;
        final int secondAgentsCount = 124;

        final CellPopulationDescription description = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                mock(Agent.class), firstAgentsCount,
                mock(Agent.class), secondAgentsCount
            )
        );

        // when


        // then

        assertThat(description.getAgentsCount()).isEqualTo(firstAgentsCount + secondAgentsCount);

    }
}