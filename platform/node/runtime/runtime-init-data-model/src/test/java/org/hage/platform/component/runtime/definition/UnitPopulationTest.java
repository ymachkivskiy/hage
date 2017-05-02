package org.hage.platform.component.runtime.definition;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class UnitPopulationTest {

    @Test
    public void shouldMergeCellPopulationDescriptionWithNonEmptyDescription() throws Exception {

        // given

        final AgentDefinition firstAgent = dummyAgentDef();
        final Integer firstAgentCount = 13;
        final AgentDefinition secondAgent = dummyAgentDef();
        final Integer secondAgentCount = 110;

        final UnitPopulation firstDescr = UnitPopulation.populationFromMap(
            ImmutableMap.of(
                firstAgent, firstAgentCount,
                secondAgent, secondAgentCount
            )
        );

        final AgentDefinition thirdAgent = dummyAgentDef();
        final Integer thirdAgentCount = 17;

        final UnitPopulation secondDescr = UnitPopulation.populationFromMap(
            ImmutableMap.of(
                thirdAgent, thirdAgentCount
            )
        );

        // when

        UnitPopulation mergedDef = firstDescr.merge(secondDescr);

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsOnly(firstAgent, secondAgent, thirdAgent);
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
        final UnitPopulation firstDescr = UnitPopulation.populationFromMap(
            ImmutableMap.of(
                firstAgentDef, 13,
                commonDef, firstCommonAgentCount
            )
        );

        final Integer secondCommonAgentCount = 17;
        final UnitPopulation secondDescr = UnitPopulation.populationFromMap(
            ImmutableMap.of(
                commonDef, secondCommonAgentCount
            )
        );

        // when

        UnitPopulation mergedDef = firstDescr.merge(secondDescr);

        // then


        assertThat(mergedDef.getAgentCountForDefinition(commonDef)).isEqualTo(firstCommonAgentCount + secondCommonAgentCount);
        assertThat(mergedDef.getAgentDefinitions()).containsOnly(firstAgentDef, commonDef);
    }

    @Test
    public void shouldMergeCellPopulationDescriptionWithEmptyCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final Integer agentCount = 13;

        final UnitPopulation firstDef = UnitPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        UnitPopulation mergedDef = firstDef.merge(UnitPopulation.emptyUnitPopulation());

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);

    }

    @Test
    public void shouldMergeEmptyPopulationDescriptionWithCellPopulationDescription() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final Integer agentCount = 13;

        final UnitPopulation firstDef = UnitPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, agentCount)
        );

        // when

        UnitPopulation mergedDef = UnitPopulation.emptyUnitPopulation().merge(firstDef);

        // then

        assertThat(mergedDef.getAgentDefinitions()).containsExactly(agentDefinition);
        assertThat(mergedDef.getAgentCountForDefinition(agentDefinition)).isEqualTo(agentCount);


    }

    @Test
    public void shouldContainNoAgents() throws Exception {

        // given

        final UnitPopulation populationDescription = UnitPopulation.emptyUnitPopulation();

        // when

        // then

        assertThat(populationDescription.getAgentDefinitions()).isEmpty();

    }

    @Test
    public void shouldContainAgents() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();

        final UnitPopulation description = UnitPopulation.populationFromMap(
            ImmutableMap.of(agentDefinition, 13)
        );

        // when


        // then

        assertThat(description.getAgentDefinitions()).containsExactly(agentDefinition);


    }

    @Test
    public void shouldReturnZeroAgentCountForMissingDefinition() throws Exception {

        // given

        final UnitPopulation description = UnitPopulation.emptyUnitPopulation();

        // when

        // then

        assertThat(description.getAgentCountForDefinition(dummyAgentDef())).isZero();


    }

    @Test
    public void shouldReturnAgentCountForDefinition() throws Exception {

        // given

        final AgentDefinition agentDefinition = dummyAgentDef();
        final int agentsCount = 13;

        final UnitPopulation description = UnitPopulation.populationFromMap(
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

        final UnitPopulation description = UnitPopulation.populationFromMap(
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
        return mock(AgentDefinition.class);
    }
}