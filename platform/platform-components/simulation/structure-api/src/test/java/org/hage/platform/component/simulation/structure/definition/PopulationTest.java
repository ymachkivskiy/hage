package org.hage.platform.component.simulation.structure.definition;

import org.hage.platform.simulation.base.Agent;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.util.Collections.set;
import static org.hage.platform.component.simulation.structure.definition.CellPopulation.populationFromMap;
import static org.hage.platform.component.simulation.structure.definition.CellPopulation.populationFromPair;
import static org.hage.platform.component.simulation.structure.definition.Population.emptyDistributionMap;
import static org.hage.platform.component.simulation.structure.definition.Position.definedBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PopulationTest {

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingNonEmptyMapWithEmptyMap() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(2, 0, 0);
        final Position pos3 = definedBy(0, 0, 3);
        final Position pos4 = definedBy(4, 0, 4);

        final Population originalDistributionMap = Population.distributionFromMap(
                of(
                        pos1, mock(CellPopulation.class),
                        pos2, mock(CellPopulation.class),
                        pos3, mock(CellPopulation.class),
                        pos4, mock(CellPopulation.class)
                )
        );

        // when

        Population mergedMap = originalDistributionMap.merge(emptyDistributionMap());

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);

    }

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingEmptyMapWithNonEmptyMap() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(2, 0, 0);
        final Position pos3 = definedBy(0, 0, 3);
        final Position pos4 = definedBy(4, 0, 4);

        final Population originalDistributionMap = Population.distributionFromMap(
                of(
                        pos1, mock(CellPopulation.class),
                        pos2, mock(CellPopulation.class),
                        pos3, mock(CellPopulation.class),
                        pos4, mock(CellPopulation.class)
                )
        );

        // when

        Population mergedMap = emptyDistributionMap().merge(originalDistributionMap);

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);


    }

    @Test
    public void shouldContainAllPositionsFromBothMapsAfterMerging() throws Exception {

        // given

        final Position pos1Map1 = definedBy(0, 0, 1);
        final Position pos2Map1 = definedBy(2, 0, 0);
        final Position pos1Map2 = definedBy(0, 0, 3);
        final Position pos2Map2 = definedBy(4, 0, 4);
        final Position pos3Map2 = definedBy(5, 1, 5);

        final Population firstMap = Population.distributionFromMap(
                of(
                        pos1Map1, mock(CellPopulation.class),
                        pos2Map1, mock(CellPopulation.class)
                )
        );

        final Population secondMap = Population.distributionFromMap(
                of(
                        pos1Map2, mock(CellPopulation.class),
                        pos2Map2, mock(CellPopulation.class),
                        pos3Map2, mock(CellPopulation.class)
                )
        );


        // when

        Population mergedMap = firstMap.merge(secondMap);

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1Map1, pos2Map1, pos1Map2, pos2Map2, pos3Map2);


    }

    @Test
    public void shouldReturnEmptyListWhileSplittingWithEmptyListCriteria() throws Exception {

        // given

        Population tested = Population.distributionFromMap(
                of(
                        definedBy(0, 0, 1), mock(CellPopulation.class),
                        definedBy(0, 1, 0), mock(CellPopulation.class),
                        definedBy(1, 0, 1), mock(CellPopulation.class),
                        definedBy(1, 1, 0), mock(CellPopulation.class)
                )
        );

        // when

        List<Population> populationMaps = tested.split(emptyList());

        // then

        assertThat(populationMaps).isEmpty();

    }

    @Test
    public void shouldMergeSplittedMapIntoOriginalMap() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(0, 1, 0);
        final Position pos3 = definedBy(1, 0, 1);
        final Position pos4 = definedBy(1, 1, 0);

        final Population originalMap = Population.distributionFromMap(
                of(
                        pos1, mock(CellPopulation.class),
                        pos2, mock(CellPopulation.class),
                        pos3, mock(CellPopulation.class),
                        pos4, mock(CellPopulation.class)
                )
        );

        // when

        List<Population> distributedMaps = originalMap.split(asList(
                set(pos1, pos2),
                set(pos3, pos4)
        ));

        Population mergedMapAfterSplitting = distributedMaps.get(0).merge(distributedMaps.get(1));

        // then

        assertThat(mergedMapAfterSplitting).isEqualTo(originalMap);
    }

    @Test
    public void shouldReturnSplittedMapsForGivenSplitCriteria() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(0, 1, 0);
        final Position pos3 = definedBy(1, 0, 1);
        final Position pos4 = definedBy(1, 1, 0);

        final Population originalMap = Population.distributionFromMap(
                of(
                        pos1, mock(CellPopulation.class),
                        pos2, mock(CellPopulation.class),
                        pos3, mock(CellPopulation.class),
                        pos4, mock(CellPopulation.class)
                )
        );

        // when

        List<Population> splittedMaps = originalMap.split(asList(
                set(pos1, pos2),
                set(pos3, pos4)
        ));

        // then

        assertThat(splittedMaps).hasSize(2);
        assertThat(splittedMaps.get(0).getInternalPositions()).containsOnly(pos1, pos2);
        assertThat(splittedMaps.get(1).getInternalPositions()).containsOnly(pos3, pos4);

    }

    @Test
    public void shouldSplitIntoEmptyMapsForEmptyListOfSplitCriteria() throws Exception {

        //given

        final Population originalMap = Population.distributionFromMap(
            of(
                definedBy(0, 0, 1), mock(CellPopulation.class),
                definedBy(0, 1, 0), mock(CellPopulation.class),
                definedBy(1, 0, 1), mock(CellPopulation.class),
                definedBy(1, 1, 0), mock(CellPopulation.class)
            )
        );

        // when

        List<Population> splitted = originalMap.split(asList(emptySet(), emptySet()));

        // then

        assertThat(splitted).hasSize(2);
        assertThat(splitted.get(0)).isEqualTo(emptyDistributionMap());
        assertThat(splitted.get(1)).isEqualTo(emptyDistributionMap());

    }

    @Test
    public void shouldReturnEmptyListOfSplittedPopulationsForEmptyInputSplitList() throws Exception {

        // given

        final Population originalMap = Population.distributionFromMap(
            of(
                definedBy(0, 0, 1), mock(CellPopulation.class),
                definedBy(0, 1, 0), mock(CellPopulation.class),
                definedBy(1, 0, 1), mock(CellPopulation.class),
                definedBy(1, 1, 0), mock(CellPopulation.class)
            )
        );

        // when

        List<Population> splitted = originalMap.split(emptyList());

        // then

        assertThat(splitted).isEmpty();
    }

    @Test
    public void shouldReturnNumberOfAllConfiguredAgents() throws Exception {

        // given

        final int count1 = 1;
        final int count2 = 23;
        final int count3 = 6;
        final int count4 = 231;
        final int count5 = 12;
        final int count6 = 22;
        final int count7 = 17;
        final int count8 = 72;

        final Population distributionMap = Population.distributionFromMap(
            of(
                definedBy(0, 0, 1), populationFromMap(of(
                    dummyAgentDef(), count1,
                    dummyAgentDef(), count2
                )),
                definedBy(0, 1, 0), populationFromMap(of(
                    dummyAgentDef(), count3,
                    dummyAgentDef(), count4
                )),
                definedBy(1, 0, 1), populationFromPair(dummyAgentDef(), count5),
                definedBy(1, 1, 0), populationFromMap(of(
                    dummyAgentDef(), count6,
                    dummyAgentDef(), count7,
                    dummyAgentDef(), count8
                ))
            )
        );

        // when

        Long numberOfConfiguredAgents = distributionMap.getNumberOfAgents();

        // then

        assertThat(numberOfConfiguredAgents).isEqualTo(count1 + count2 + count3 + count4 + count5 + count6 + count7 + count8);

    }

    @Test
    public void shouldUpdatePopulationForCommonInternalPositionsDuringMergingMaps() throws Exception {

        // given

        final Position commonInternalPos = definedBy(2, 0, 0);

        final CellPopulation firstMapCommonCellPopulation = mock(CellPopulation.class);
        final CellPopulation secondMapCommonCellPopulation = mock(CellPopulation.class);
        final CellPopulation expectedMergedCellPopulation = mock(CellPopulation.class);

        when(firstMapCommonCellPopulation.merge(secondMapCommonCellPopulation)).thenReturn(expectedMergedCellPopulation);
        when(secondMapCommonCellPopulation.merge(firstMapCommonCellPopulation)).thenReturn(expectedMergedCellPopulation);

        final Population firstMap = Population.distributionFromMap(
                of(
                        definedBy(0, 0, 1), mock(CellPopulation.class),
                        commonInternalPos, firstMapCommonCellPopulation
                )
        );

        final Population secondMap = Population.distributionFromMap(
                of(
                        commonInternalPos, secondMapCommonCellPopulation,
                        definedBy(4, 0, 4), mock(CellPopulation.class)
                )
        );


        // when

        Population mergedMap = firstMap.merge(secondMap);

        // then

        assertThat(mergedMap.getPopulationFor(commonInternalPos)).isSameAs(expectedMergedCellPopulation);

    }

    @Test
    public void shouldReturnEmptyListOfInternalPositionsForEmptyMap() throws Exception {

        // given

        final Population tested = emptyDistributionMap();

        // when

        Set<Position> positions = tested.getInternalPositions();

        // then

        assertThat(positions).isEmpty();

    }

    @Test
    public void shouldReturnInternalPositionsForMap() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(2, 0, 0);
        final Position pos3 = definedBy(4, 0, 4);

        final Population testedMap = Population.distributionFromMap(
            of(
                pos1, mock(CellPopulation.class),
                pos2, mock(CellPopulation.class),
                pos3, mock(CellPopulation.class)
            )
        );


        // when

        Set<Position> positions = testedMap.getInternalPositions();

        // then

        assertThat(positions).containsOnly(pos1, pos2, pos3);


    }


    @Test
    public void shouldReturnEmptyCellPopulationForInternalPositionNotFromMap() throws Exception {

        // given

        final Population tested = Population.distributionFromMap(
                of(
                        definedBy(1, 2, 3), mock(CellPopulation.class),
                        definedBy(1, 1, 3), mock(CellPopulation.class)
                )
        );

        // when

        CellPopulation populationDescription = tested.getPopulationFor(definedBy(0, 0, 0));

        // then

        assertThat(populationDescription).isSameAs(CellPopulation.emptyPopulation());

    }

    @Test
    public void shouldReturnCellPopulationForInternalPosition() throws Exception {

        // given

        final Position pos1 = definedBy(0, 0, 1);
        final Position pos2 = definedBy(2, 0, 0);
        final Position pos3 = definedBy(4, 0, 4);

        final CellPopulation pos1Population = mock(CellPopulation.class);
        final CellPopulation pos2Population = mock(CellPopulation.class);
        final CellPopulation pos3Population = mock(CellPopulation.class);

        final Population testedMap = Population.distributionFromMap(
                of(
                        pos1, pos1Population,
                        pos2, pos2Population,
                        pos3, pos3Population
                )
        );


        // when

        // then

        assertThat(testedMap.getPopulationFor(pos1)).isSameAs(pos1Population);
        assertThat(testedMap.getPopulationFor(pos2)).isSameAs(pos2Population);
        assertThat(testedMap.getPopulationFor(pos3)).isSameAs(pos3Population);

    }

    private static AgentDefinition dummyAgentDef() {
        return new AgentDefinition(Agent.class);
    }
}