package org.hage.platform.config.local.gen;

import org.fest.assertions.Condition;
import org.hage.platform.config.CellPopulationDescription;
import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.agent.AgentCountData;
import org.hage.platform.config.def.agent.ChunkAgentDistribution;
import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.config.local.util.ChunkPositionsConditions;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.*;
import static org.hage.platform.config.def.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.config.def.agent.PositionsSelectionData.randomPositions;
import static org.hage.platform.habitat.structure.Dimensions.of;
import static org.hage.platform.habitat.structure.InternalPosition.definedBy;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/org/hage/platform/config/local/module-spring-conf.xml")
public class MergingPopulationDistributionMapCreatorE2ETest {

    @Autowired
    private MergingPopulationDistributionMapCreator tested;

    // region no chunkAgentDistribution

    @Test
    public void shouldCreateEmptyDistributionMapForChunkQualifierWithEmptyChunkAgentDistribution() throws Exception {

        // given

        ChunkPopulationQualifier chunkQualifier = CREATE_QUALIFIER(new Chunk(null, null));

        // when

        PopulationDistributionMap distributionMap = tested.createMap(chunkQualifier);

        // then

        assertThat(distributionMap.getInternalPositions()).isEmpty();


    }
    // endregion

    // region one chunkAgentDistribution

    @Test
    public void shouldCreateDistributionMapWithOneAgentDefinitionInAllCellPopulationDescriptions() throws Exception {

        // given

        final AgentDefinition expectedAgentDefinition = mock(AgentDefinition.class);

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(31, 10, 0), of(52, 11, 27)),
            new ChunkAgentDistribution(
                expectedAgentDefinition,
                fixed(1),
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then


        assertThat(distributionMap).satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
            celPopDescr -> celPopDescr.getAgentDefinitions().size() == 1 && celPopDescr.getAgentDefinitions().contains(expectedAgentDefinition)
        ));

    }

    @Test
    public void shouldCreateDistributionMapWithFixedCountOfAgents() throws Exception {

        // given

        final int expectedAgentCount = 100;
        final AgentCountData COUNT_DATA = fixed(expectedAgentCount);

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(0, 10, 0), of(22, 7, 5)),
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) == expectedAgentCount
            ));
    }

    @Test
    public void shouldCreateDistributionMapWithAtLeastCountOfAgents() throws Exception {

        // given

        final int minExpectedAgentsCount = 139;
        final AgentCountData COUNT_DATA = atLeast(minExpectedAgentsCount);

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(0, 0, 13), of(2, 10, 31)),
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) >= minExpectedAgentsCount
            ));

    }


    @Test
    public void shouldCreateDistributionMapWithAtMostCountOfAgents() throws Exception {

        // given

        final int maxExpectingAgents = 1103;
        final AgentCountData COUNT_DATA = atMost(maxExpectingAgents);

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(112, 17, 13), of(53, 13, 3)),
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) > 0 && popDescr.getAgentCountForDefinition(agentDefinition) <= maxExpectingAgents
            ));

    }

    @Test
    public void shouldCreateDistributionMapWithBetweenCountOfAgents() throws Exception {

        // given

        final int minExpectingAgents = 123;
        final int maxExpectingAgents = 1103;
        final AgentCountData COUNT_DATA = between(minExpectingAgents, maxExpectingAgents);

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(112, 7, 13), of(43, 13, 3)),
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) >= minExpectingAgents && popDescr.getAgentCountForDefinition(agentDefinition) <= maxExpectingAgents
            ));


    }

    @Test
    public void shouldCreateDistributionMapWithRandomPositiveCountOfAgents() throws Exception {

        // given

        final AgentCountData COUNT_DATA = random();

        final AgentDefinition agentDefinition = mock(AgentDefinition.class);
        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(12, 171, 133), of(3, 33, 12)),
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) >= 0
            ));


    }

    @Test
    public void shouldCreateDistributionMapWithAllInternalPositions() throws Exception {

        // given

        final PositionsSelectionData POSITIONS_SELECTION_DATA = allPositions();
        final Chunk chunk = new Chunk(definedBy(12, 171, 133), of(32, 33, 12));

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                mock(AgentDefinition.class),
                fixed(1),
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions()).isEqualTo(chunk.getInternalPositions());

    }

    @Test
    public void shouldCreateDistributionMapWithRandomPositiveNumberOfRandomInternalPositionsOfChunk() throws Exception {

        // given

        final PositionsSelectionData POSITIONS_SELECTION_DATA = randomPositions();
        final Chunk chunk = new Chunk(definedBy(1222, 11, 1), of(2, 323, 112));

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                mock(AgentDefinition.class),
                fixed(1),
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions().size()).isGreaterThanOrEqualTo(1);
        assertThat(distributionMap.getInternalPositions()).satisfies(ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));

    }

    @Test
    public void shouldCreateDistributionMapWithFixedNumberOfRandomInternalPositionsOfChunk() throws Exception {

        // given

        final int numberOfPositions = 105;
        final PositionsSelectionData POSITIONS_SELECTION_DATA = randomPositions(numberOfPositions);
        final Chunk chunk = new Chunk(definedBy(1222, 11, 1), of(2, 323, 112));

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                mock(AgentDefinition.class),
                fixed(1),
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions().size()).isGreaterThanOrEqualTo(numberOfPositions);
        assertThat(distributionMap.getInternalPositions()).satisfies(ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));


    }

    @Test
    public void shouldCreateDistributionMapAllInternalPositionsOfChunckWhenFixedNumberIsGreaterOrEqualThanChunkSize() throws Exception {

        // given

        final Chunk chunk = new Chunk(definedBy(1222, 11, 1), of(2, 33, 12));
        final long numberOfPositions = chunk.getSize() + 10;
        final PositionsSelectionData POSITIONS_SELECTION_DATA = randomPositions(numberOfPositions);

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                mock(AgentDefinition.class),
                fixed(1),
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions()).isEqualTo(chunk.getInternalPositions());

    }

    // region special cases

    @Test
    public void shouldCreateDistributionMapWithAgentNumberBetweenInRandomChoosenFixedCountInternalPositions() throws Exception {

        // given

        final int minCount = 15;
        final int maxCount = 17;
        long numberOfPositions = 109;

        final PositionsSelectionData POSITIONS_SELECTION_DATA = randomPositions(numberOfPositions);
        final AgentCountData COUNT_DATA = between(minCount, maxCount);
        final Chunk chunk = new Chunk(definedBy(2, 11, 1), of(17, 33, 12));
        final AgentDefinition agentDefinition = mock(AgentDefinition.class);

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions().size()).isEqualTo((int) numberOfPositions);
        assertThat(distributionMap.getInternalPositions()).satisfies(ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));
        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) >= minCount && popDescr.getAgentCountForDefinition(agentDefinition) <= maxCount
            ));

    }


    @Test
    public void shouldCreateDistributionMapWithAtLeastAgentNumberInRandomChoosenRandomInternalPositions() throws Exception {

        // given

        final int minCount = 133;

        final PositionsSelectionData POSITIONS_SELECTION_DATA = randomPositions();
        final AgentCountData COUNT_DATA = atLeast(minCount);
        final Chunk chunk = new Chunk(definedBy(2, 11, 1), of(177, 3, 7));
        final AgentDefinition agentDefinition = mock(AgentDefinition.class);

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            chunk,
            new ChunkAgentDistribution(
                agentDefinition,
                COUNT_DATA,
                POSITIONS_SELECTION_DATA
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap.getInternalPositions().size()).isGreaterThanOrEqualTo(1);
        assertThat(distributionMap.getInternalPositions()).satisfies(ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));
        assertThat(distributionMap)
            .satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
                popDescr -> popDescr.getAgentCountForDefinition(agentDefinition) >= minCount
            ));

    }

    // endregion

    // endregion

    //region many chunkAgentDistributions

    @Test
    public void shouldCreateDistributionMapWithAllInternalPositionsContainsFixedNumberOfTwoTypesOfAgents() throws Exception {

        // given

        final int firstAgentsCount = 111;
        final AgentDefinition firstAgentDefinition = mock(AgentDefinition.class);
        final int secondAgentsCount = 37;
        final AgentDefinition secondAgentDefinition = mock(AgentDefinition.class);


        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(2, 11, 1), of(77, 5, 17)),
            new ChunkAgentDistribution(
                firstAgentDefinition,
                fixed(firstAgentsCount),
                allPositions()
            ),
            new ChunkAgentDistribution(
                secondAgentDefinition,
                fixed(secondAgentsCount),
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap).satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
            popDescr ->
                popDescr.getAgentCountForDefinition(firstAgentDefinition) == firstAgentsCount
                    &&
                    popDescr.getAgentCountForDefinition(secondAgentDefinition) == secondAgentsCount
        ));

    }

    @Test
    public void shouldCreateDistributionMapWithInternalPositionsWithTwoTypesOfAgentsOfSomeNumber() throws Exception {

        // given

        final AgentDefinition firstAgentDefinition = mock(AgentDefinition.class);
        final AgentDefinition secondAgentDefinition = mock(AgentDefinition.class);


        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(2, 11, 1), of(77, 5, 17)),
            new ChunkAgentDistribution(
                firstAgentDefinition,
                random(),
                allPositions()
            ),
            new ChunkAgentDistribution(
                secondAgentDefinition,
                random(),
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap).satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
            popDescr ->
                popDescr.getAgentCountForDefinition(firstAgentDefinition) > 0
                    &&
                    popDescr.getAgentCountForDefinition(secondAgentDefinition) > 0
        ));


    }

    @Test
    public void shouldCreateDistributionMapWithSomeInternalPositionsWithThreeTypesOfAgents() throws Exception {

        // given

        final int firstAgentsCount = 111;
        final AgentDefinition firstAgentDefinition = mock(AgentDefinition.class);
        final AgentDefinition secondAgentDefinition = mock(AgentDefinition.class);
        final AgentDefinition thirdAgentDefinition = mock(AgentDefinition.class);
        final int thirdMin = 17;
        final int thirdMax = 18;

        final ChunkPopulationQualifier qualifier = CREATE_QUALIFIER(
            new Chunk(definedBy(2, 11, 1), of(77, 5, 17)),
            new ChunkAgentDistribution(
                firstAgentDefinition,
                fixed(firstAgentsCount),
                allPositions()
            ),
            new ChunkAgentDistribution(
                secondAgentDefinition,
                random(),
                allPositions()
            ),
            new ChunkAgentDistribution(
                thirdAgentDefinition,
                between(thirdMin, thirdMax),
                allPositions()
            )
        );

        // when

        PopulationDistributionMap distributionMap = tested.createMap(qualifier);

        // then

        assertThat(distributionMap).satisfies(ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(
            popDescr ->
                popDescr.getAgentCountForDefinition(firstAgentDefinition) == firstAgentsCount
                    &&
                    popDescr.getAgentCountForDefinition(secondAgentDefinition) > 0
                    &&
                    popDescr.getAgentCountForDefinition(thirdAgentDefinition) >= thirdMin && popDescr.getAgentCountForDefinition(thirdAgentDefinition) <= thirdMax
        ));

    }

    //endregion

    private static ChunkPopulationQualifier CREATE_QUALIFIER(Chunk chunk, ChunkAgentDistribution... agentDistributions) {
        return new ChunkPopulationQualifier(chunk, asList(agentDistributions));
    }

    private Condition<Object> ALL_CELL_POPULATIONS_IN_MAP_IS_MATCHING(Predicate<CellPopulationDescription> predicate) {
        return new Condition<Object>() {
            @Override
            public boolean matches(Object o) {
                PopulationDistributionMap map = (PopulationDistributionMap) o;

                for (InternalPosition internalPosition : map.getInternalPositions()) {
                    CellPopulationDescription pop = map.getPopulationFor(internalPosition);
                    if (!predicate.test(pop)) return false;
                }

                return true;
            }
        };
    }



}