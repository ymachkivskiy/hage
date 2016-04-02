package org.hage.platform.component.runtime.definition;

import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.Agent;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.util.Collections.set;
import static org.hage.platform.component.runtime.init.Population.emptyPopulation;
import static org.hage.platform.component.runtime.init.UnitPopulation.populationFromMap;
import static org.hage.platform.component.runtime.init.UnitPopulation.populationFromPair;
import static org.hage.platform.component.structure.Position.position;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PopulationTest {

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingNonEmptyMapWithEmptyMap() throws Exception {

        // given

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(0, 2, 0);
        final Position pos3 = position(0, 0, 3);
        final Position pos4 = position(0, 4, 4);

        final Population originalDistributionMap = Population.populationFromMap(
                of(
                        pos1, mock(UnitPopulation.class),
                        pos2, mock(UnitPopulation.class),
                        pos3, mock(UnitPopulation.class),
                        pos4, mock(UnitPopulation.class)
                )
        );

        // when

        Population mergedMap = originalDistributionMap.merge(emptyPopulation());

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);

    }

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingEmptyMapWithNonEmptyMap() throws Exception {

        // given

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(0, 2, 0);
        final Position pos3 = position(0, 0, 3);
        final Position pos4 = position(0, 4, 4);

        final Population originalDistributionMap = Population.populationFromMap(
                of(
                        pos1, mock(UnitPopulation.class),
                        pos2, mock(UnitPopulation.class),
                        pos3, mock(UnitPopulation.class),
                        pos4, mock(UnitPopulation.class)
                )
        );

        // when

        Population mergedMap = emptyPopulation().merge(originalDistributionMap);

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);


    }

    @Test
    public void shouldContainAllPositionsFromBothMapsAfterMerging() throws Exception {

        // given

        final Position pos1Map1 = position(0, 0, 1);
        final Position pos2Map1 = position(0, 2, 0);
        final Position pos1Map2 = position(0, 0, 3);
        final Position pos2Map2 = position(0, 4, 4);
        final Position pos3Map2 = position(1, 5, 5);

        final Population firstMap = Population.populationFromMap(
                of(
                        pos1Map1, mock(UnitPopulation.class),
                        pos2Map1, mock(UnitPopulation.class)
                )
        );

        final Population secondMap = Population.populationFromMap(
                of(
                        pos1Map2, mock(UnitPopulation.class),
                        pos2Map2, mock(UnitPopulation.class),
                        pos3Map2, mock(UnitPopulation.class)
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

        Population tested = Population.populationFromMap(
                of(
                        position(0, 0, 1), mock(UnitPopulation.class),
                        position(1, 0, 0), mock(UnitPopulation.class),
                        position(0, 1, 1), mock(UnitPopulation.class),
                        position(1, 1, 0), mock(UnitPopulation.class)
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

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(1, 0, 0);
        final Position pos3 = position(0, 1, 1);
        final Position pos4 = position(1, 1, 0);

        final Population originalMap = Population.populationFromMap(
                of(
                        pos1, mock(UnitPopulation.class),
                        pos2, mock(UnitPopulation.class),
                        pos3, mock(UnitPopulation.class),
                        pos4, mock(UnitPopulation.class)
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

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(1, 0, 0);
        final Position pos3 = position(0, 1, 1);
        final Position pos4 = position(1, 1, 0);

        final Population originalMap = Population.populationFromMap(
                of(
                        pos1, mock(UnitPopulation.class),
                        pos2, mock(UnitPopulation.class),
                        pos3, mock(UnitPopulation.class),
                        pos4, mock(UnitPopulation.class)
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

        final Population originalMap = Population.populationFromMap(
            of(
                position(0, 0, 1), mock(UnitPopulation.class),
                position(1, 0, 0), mock(UnitPopulation.class),
                position(0, 1, 1), mock(UnitPopulation.class),
                position(1, 1, 0), mock(UnitPopulation.class)
            )
        );

        // when

        List<Population> splitted = originalMap.split(asList(emptySet(), emptySet()));

        // then

        assertThat(splitted).hasSize(2);
        assertThat(splitted.get(0)).isEqualTo(emptyPopulation());
        assertThat(splitted.get(1)).isEqualTo(emptyPopulation());

    }

    @Test
    public void shouldReturnEmptyListOfSplittedPopulationsForEmptyInputSplitList() throws Exception {

        // given

        final Population originalMap = Population.populationFromMap(
            of(
                position(0, 0, 1), mock(UnitPopulation.class),
                position(1, 0, 0), mock(UnitPopulation.class),
                position(0, 1, 1), mock(UnitPopulation.class),
                position(1, 1, 0), mock(UnitPopulation.class)
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

        final Population distributionMap = Population.populationFromMap(
            of(
                position(0, 0, 1), populationFromMap(of(
                    dummyAgentDef(), count1,
                    dummyAgentDef(), count2
                )),
                position(1, 0, 0), populationFromMap(of(
                    dummyAgentDef(), count3,
                    dummyAgentDef(), count4
                )),
                position(0, 1, 1), populationFromPair(dummyAgentDef(), count5),
                position(1, 1, 0), populationFromMap(of(
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

        final Position commonInternalPos = position(0, 2, 0);

        final UnitPopulation firstMapCommonUnitPopulation = mock(UnitPopulation.class);
        final UnitPopulation secondMapCommonUnitPopulation = mock(UnitPopulation.class);
        final UnitPopulation expectedMergedUnitPopulation = mock(UnitPopulation.class);

        when(firstMapCommonUnitPopulation.merge(secondMapCommonUnitPopulation)).thenReturn(expectedMergedUnitPopulation);
        when(secondMapCommonUnitPopulation.merge(firstMapCommonUnitPopulation)).thenReturn(expectedMergedUnitPopulation);

        final Population firstMap = Population.populationFromMap(
                of(
                        position(0, 0, 1), mock(UnitPopulation.class),
                        commonInternalPos, firstMapCommonUnitPopulation
                )
        );

        final Population secondMap = Population.populationFromMap(
                of(
                        commonInternalPos, secondMapCommonUnitPopulation,
                        position(0, 4, 4), mock(UnitPopulation.class)
                )
        );


        // when

        Population mergedMap = firstMap.merge(secondMap);

        // then

        assertThat(mergedMap.unitPopulationFor(commonInternalPos)).isSameAs(expectedMergedUnitPopulation);

    }

    @Test
    public void shouldReturnEmptyListOfInternalPositionsForEmptyMap() throws Exception {

        // given

        final Population tested = emptyPopulation();

        // when

        Set<Position> positions = tested.getInternalPositions();

        // then

        assertThat(positions).isEmpty();

    }

    @Test
    public void shouldReturnInternalPositionsForMap() throws Exception {

        // given

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(0, 2, 0);
        final Position pos3 = position(0, 4, 4);

        final Population testedMap = Population.populationFromMap(
            of(
                pos1, mock(UnitPopulation.class),
                pos2, mock(UnitPopulation.class),
                pos3, mock(UnitPopulation.class)
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

        final Population tested = Population.populationFromMap(
                of(
                        position(2, 1, 3), mock(UnitPopulation.class),
                        position(1, 1, 3), mock(UnitPopulation.class)
                )
        );

        // when

        UnitPopulation populationDescription = tested.unitPopulationFor(position(0, 0, 0));

        // then

        assertThat(populationDescription).isSameAs(UnitPopulation.emptyUnitPopulation());

    }

    @Test
    public void shouldReturnCellPopulationForInternalPosition() throws Exception {

        // given

        final Position pos1 = position(0, 0, 1);
        final Position pos2 = position(0, 2, 0);
        final Position pos3 = position(0, 4, 4);

        final UnitPopulation pos1Population = mock(UnitPopulation.class);
        final UnitPopulation pos2Population = mock(UnitPopulation.class);
        final UnitPopulation pos3Population = mock(UnitPopulation.class);

        final Population testedMap = Population.populationFromMap(
                of(
                        pos1, pos1Population,
                        pos2, pos2Population,
                        pos3, pos3Population
                )
        );


        // when

        // then

        assertThat(testedMap.unitPopulationFor(pos1)).isSameAs(pos1Population);
        assertThat(testedMap.unitPopulationFor(pos2)).isSameAs(pos2Population);
        assertThat(testedMap.unitPopulationFor(pos3)).isSameAs(pos3Population);

    }

    private static AgentDefinition dummyAgentDef() {
        return new AgentDefinition(Agent.class);
    }
}