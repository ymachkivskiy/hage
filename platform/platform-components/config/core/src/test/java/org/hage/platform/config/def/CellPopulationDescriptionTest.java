package org.hage.platform.config.def;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.habitat.AgentDefinition;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CellPopulationDescriptionTest {

    @Test
    public void shouldMergeCellPopulationDescriptionWithNonEmptyDescription() throws Exception {

        // given

        final AgentDefinition firstAgentDefinition = mock(AgentDefinition.class);
        final Integer firstAgentCount = 13;
        final AgentDefinition secondAgentDefinition = mock(AgentDefinition.class);
        final Integer secondAgentCount = 110;

        final CellPopulationDescription firstDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                firstAgentDefinition, firstAgentCount,
                secondAgentDefinition, secondAgentCount
            )
        );

        final AgentDefinition thirdAgentDefinition = mock(AgentDefinition.class);
        final Integer thirdAgentCount = 17;

        final CellPopulationDescription secondDescr = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(
                thirdAgentDefinition, thirdAgentCount
            )
        );

        // when

        CellPopulationDescription mergedDef = firstDescr.merge(secondDescr);

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsOnly(firstAgentDefinition, secondAgentDefinition, thirdAgentDefinition);
        assertThat(mergedDef.getAgentsCount()).isEqualTo(firstAgentCount + secondAgentCount + thirdAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(firstAgentDefinition)).isEqualTo(firstAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(secondAgentDefinition)).isEqualTo(secondAgentCount);
        assertThat(mergedDef.getAgentCountForDefinition(thirdAgentDefinition)).isEqualTo(thirdAgentCount);


    }

    @Test
    public void shouldMergeCellPopulationDescriptionsWithSameAgentDefinitions() throws Exception {

        // given

        final AgentDefinition commonDef = mock(AgentDefinition.class);
        final AgentDefinition firstAgentDef = mock(AgentDefinition.class);

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
        assertThat(mergedDef.getAgentDefinitions()).containsOnly(firstAgentDef, commonDef);
    }

    @Test
    public void shouldMergeCellPopulationDescriptionWithEmptyCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final Integer agentCount = 13;

        final CellPopulationDescription firstDef = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulationDescription mergedDef = firstDef.merge(CellPopulationDescription.emptyPopulation());

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);

    }

    @Test
    public void shouldMergeEmptyPopulationDescriptionWithCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final Integer agentCount = 13;

        final CellPopulationDescription firstDef = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulationDescription mergedDef = CellPopulationDescription.emptyPopulation().merge(firstDef);

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);


    }

    @Test
    public void shouldContainNoAgentDefinitions() throws Exception {

        // given

        final CellPopulationDescription populationDescription = CellPopulationDescription.emptyPopulation();

        // when

        // then

        assertThat(populationDescription.getAgentDefinitions()).isEmpty();

    }

    @Test
    public void shouldContainAgentDefinitions() throws Exception {

        // given

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);

        final CellPopulationDescription description = CellPopulationDescription.populationFromMap(
            ImmutableMap.of(agentDefinition, 13)
        );

        // when


        // then

        assertThat(description.getAgentDefinitions()).containsExactly(agentDefinition);


    }

    @Test
    public void shouldReturnZeroAgentCountForMissingDefinition() throws Exception {

        // given

        final CellPopulationDescription description = CellPopulationDescription.emptyPopulation();

        // when

        // then

        assertThat(description.getAgentCountForDefinition(mock(AgentDefinition.class))).isZero();


    }

    @Test
    public void shouldReturnAgentCountForDefinition() throws Exception {

        // given

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
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
                mock(AgentDefinition.class), firstAgentsCount,
                mock(AgentDefinition.class), secondAgentsCount
            )
        );

        // when


        // then

        assertThat(description.getAgentsCount()).isEqualTo(firstAgentsCount + secondAgentsCount);

    }
}