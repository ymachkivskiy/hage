package org.hage.platform.component.simulation.structure.definition;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.simulation.base.Agent;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CellPopulationTest {

    @Test
    public void shouldMergeCellPopulationDescriptionWithNonEmptyDescription() throws Exception {

        // given

        final AgentDefinition firstAgent = dummyAgentDef();
        final Integer firstAgentCount = 13;
        final AgentDefinition secondAgent = dummyAgentDef();
        final Integer secondAgentCount = 110;

        final CellPopulation firstDescr = CellPopulation.populationFromMap(
            ImmutableMap.of(
                firstAgent, firstAgentCount,
                secondAgent, secondAgentCount
            )
        );

        final AgentDefinition thirdAgent = dummyAgentDef();
        final Integer thirdAgentCount = 17;

        final CellPopulation secondDescr = CellPopulation.populationFromMap(
            ImmutableMap.of(
                thirdAgent, thirdAgentCount
            )
        );

        // when

        CellPopulation mergedDef = firstDescr.merge(secondDescr);

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

        final AgentDefinition commonDef = dummyAgentDef();
        final AgentDefinition firstAgentDef = dummyAgentDef();

        final Integer firstCommonAgentCount = 110;
        final CellPopulation firstDescr = CellPopulation.populationFromMap(
            ImmutableMap.of(
                firstAgentDef, 13,
                commonDef, firstCommonAgentCount
            )
        );

        final Integer secondCommonAgentCount = 17;
        final CellPopulation secondDescr = CellPopulation.populationFromMap(
            ImmutableMap.of(
                commonDef, secondCommonAgentCount
            )
        );

        // when

        CellPopulation mergedDef = firstDescr.merge(secondDescr);

        // then


        assertThat(mergedDef.getAgentCountForDefinition(commonDef)).isEqualTo(firstCommonAgentCount + secondCommonAgentCount);
        assertThat(mergedDef.getAgents()).containsOnly(firstAgentDef, commonDef);
    }

    @Test
    public void shouldMergeCellPopulationDescriptionWithEmptyCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final Integer agentCount = 13;

        final CellPopulation firstDef = CellPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulation mergedDef = firstDef.merge(CellPopulation.emptyPopulation());

        // then

        assertThat(mergedDef.getAgents()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);

    }

    @Test
    public void shouldMergeEmptyPopulationDescriptionWithCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final Integer agentCount = 13;

        final CellPopulation firstDef = CellPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        CellPopulation mergedDef = CellPopulation.emptyPopulation().merge(firstDef);

        // then

        assertThat(mergedDef.getAgents()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);


    }

    @Test
    public void shouldContainNoAgents() throws Exception {

        // given

        final CellPopulation populationDescription = CellPopulation.emptyPopulation();

        // when

        // then

        assertThat(populationDescription.getAgents()).isEmpty();

    }

    @Test
    public void shouldContainAgents() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();

        final CellPopulation description = CellPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, 13)
        );

        // when


        // then

        assertThat(description.getAgents()).containsExactly(agentDefinition);


    }

    @Test
    public void shouldReturnZeroAgentCountForMissingDefinition() throws Exception {

        // given

        final CellPopulation description = CellPopulation.emptyPopulation();

        // when

        // then

        assertThat(description.getAgentCountForDefinition(dummyAgentDef())).isZero();


    }

    @Test
    public void shouldReturnAgentCountForDefinition() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final int agentsCount = 13;

        final CellPopulation description = CellPopulation.populationFromMap(
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

        final CellPopulation description = CellPopulation.populationFromMap(
            ImmutableMap.of(
                dummyAgentDef(), firstAgentsCount,
                dummyAgentDef(), secondAgentsCount
            )
        );

        // when


        // then

        assertThat(description.getAgentsCount()).isEqualTo(firstAgentsCount + secondAgentsCount);

    }

    private static AgentDefinition dummyAgentDef() {
        return new AgentDefinition(Agent.class);
    }
}