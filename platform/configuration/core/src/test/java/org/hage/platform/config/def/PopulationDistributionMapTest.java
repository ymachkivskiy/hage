package org.hage.platform.config.def;

import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.util.Collections.set;
import static org.hage.platform.config.def.CellPopulationDescription.emptyPopulation;
import static org.hage.platform.config.def.CellPopulationDescription.populationFromMap;
import static org.hage.platform.config.def.CellPopulationDescription.populationFromPair;
import static org.hage.platform.config.def.PopulationDistributionMap.emptyDistributionMap;
import static org.hage.platform.habitat.structure.InternalPosition.definedBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PopulationDistributionMapTest {

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingNonEmptyMapWithEmptyMap() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(2, 0, 0);
        final InternalPosition pos3 = definedBy(0, 0, 3);
        final InternalPosition pos4 = definedBy(4, 0, 4);

        final PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        PopulationDistributionMap mergedMap = originalDistributionMap.merge(emptyDistributionMap());

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);

    }

    @Test
    public void shouldContainAllPositionsFromNonEmptyDistributionMapAfterMergingEmptyMapWithNonEmptyMap() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(2, 0, 0);
        final InternalPosition pos3 = definedBy(0, 0, 3);
        final InternalPosition pos4 = definedBy(4, 0, 4);

        final PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        PopulationDistributionMap mergedMap = emptyDistributionMap().merge(originalDistributionMap);

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1, pos2, pos3, pos4);


    }

    @Test
    public void shouldContainAllPositionsFromBothMapsAfterMerging() throws Exception {

        // given

        final InternalPosition pos1Map1 = definedBy(0, 0, 1);
        final InternalPosition pos2Map1 = definedBy(2, 0, 0);
        final InternalPosition pos1Map2 = definedBy(0, 0, 3);
        final InternalPosition pos2Map2 = definedBy(4, 0, 4);
        final InternalPosition pos3Map2 = definedBy(5, 1, 5);

        final PopulationDistributionMap firstMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1Map1, mock(CellPopulationDescription.class),
                        pos2Map1, mock(CellPopulationDescription.class)
                )
        );

        final PopulationDistributionMap secondMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1Map2, mock(CellPopulationDescription.class),
                        pos2Map2, mock(CellPopulationDescription.class),
                        pos3Map2, mock(CellPopulationDescription.class)
                )
        );


        // when

        PopulationDistributionMap mergedMap = firstMap.merge(secondMap);

        // then

        assertThat(mergedMap.getInternalPositions()).containsOnly(pos1Map1, pos2Map1, pos1Map2, pos2Map2, pos3Map2);


    }

    @Test
    public void shouldReturnEmptyListWhileSplittingWithEmptyListCriteria() throws Exception {

        // given

        PopulationDistributionMap tested = PopulationDistributionMap.distributionFromMap(
                of(
                        definedBy(0, 0, 1), mock(CellPopulationDescription.class),
                        definedBy(0, 1, 0), mock(CellPopulationDescription.class),
                        definedBy(1, 0, 1), mock(CellPopulationDescription.class),
                        definedBy(1, 1, 0), mock(CellPopulationDescription.class)
                )
        );

        // when

        List<PopulationDistributionMap> populationMaps = tested.split(emptyList());

        // then

        assertThat(populationMaps).isEmpty();

    }

    @Test
    public void shouldMergeSplittedMapIntoOriginalMap() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(0, 1, 0);
        final InternalPosition pos3 = definedBy(1, 0, 1);
        final InternalPosition pos4 = definedBy(1, 1, 0);

        final PopulationDistributionMap originalMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        List<PopulationDistributionMap> distributedMaps = originalMap.split(asList(
                set(pos1, pos2),
                set(pos3, pos4)
        ));

        PopulationDistributionMap mergedMapAfterSplitting = distributedMaps.get(0).merge(distributedMaps.get(1));

        // then

        assertThat(mergedMapAfterSplitting).isEqualTo(originalMap);
    }

    @Test
    public void shouldReturnSplittedMapsForGivenSplitCriteria() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(0, 1, 0);
        final InternalPosition pos3 = definedBy(1, 0, 1);
        final InternalPosition pos4 = definedBy(1, 1, 0);

        final PopulationDistributionMap originalMap = PopulationDistributionMap.distributionFromMap(
                of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        List<PopulationDistributionMap> splittedMaps = originalMap.split(asList(
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

        final PopulationDistributionMap originalMap = PopulationDistributionMap.distributionFromMap(
            of(
                definedBy(0, 0, 1), mock(CellPopulationDescription.class),
                definedBy(0, 1, 0), mock(CellPopulationDescription.class),
                definedBy(1, 0, 1), mock(CellPopulationDescription.class),
                definedBy(1, 1, 0), mock(CellPopulationDescription.class)
            )
        );

        // when

        List<PopulationDistributionMap> splitted = originalMap.split(asList(emptySet(), emptySet()));

        // then

        assertThat(splitted).hasSize(2);
        assertThat(splitted.get(0)).isEqualTo(emptyDistributionMap());
        assertThat(splitted.get(1)).isEqualTo(emptyDistributionMap());

    }

    @Test
    public void shouldReturnEmptyListOfSplittedPopulationsForEmptyInputSplitList() throws Exception {

        // given

        final PopulationDistributionMap originalMap = PopulationDistributionMap.distributionFromMap(
            of(
                definedBy(0, 0, 1), mock(CellPopulationDescription.class),
                definedBy(0, 1, 0), mock(CellPopulationDescription.class),
                definedBy(1, 0, 1), mock(CellPopulationDescription.class),
                definedBy(1, 1, 0), mock(CellPopulationDescription.class)
            )
        );

        // when

        List<PopulationDistributionMap> splitted = originalMap.split(emptyList());

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

        final PopulationDistributionMap distributionMap = PopulationDistributionMap.distributionFromMap(
            of(
                definedBy(0, 0, 1), populationFromMap(of(
                    mock(AgentDefinition.class), count1,
                    mock(AgentDefinition.class), count2
                )),
                definedBy(0, 1, 0), populationFromMap(of(
                    mock(AgentDefinition.class), count3,
                    mock(AgentDefinition.class), count4
                )),
                definedBy(1, 0, 1), populationFromPair(mock(AgentDefinition.class), count5),
                definedBy(1, 1, 0), populationFromMap(of(
                    mock(AgentDefinition.class), count6,
                    mock(AgentDefinition.class), count7,
                    mock(AgentDefinition.class), count8
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

        final InternalPosition commonInternalPos = definedBy(2, 0, 0);

        final CellPopulationDescription firstMapCommonCellPopulationDescription = mock(CellPopulationDescription.class);
        final CellPopulationDescription secondMapCommonCellPopulationDescription = mock(CellPopulationDescription.class);
        final CellPopulationDescription expectedMergedCellPopulationDescription = mock(CellPopulationDescription.class);

        when(firstMapCommonCellPopulationDescription.merge(secondMapCommonCellPopulationDescription)).thenReturn(expectedMergedCellPopulationDescription);
        when(secondMapCommonCellPopulationDescription.merge(firstMapCommonCellPopulationDescription)).thenReturn(expectedMergedCellPopulationDescription);

        final PopulationDistributionMap firstMap = PopulationDistributionMap.distributionFromMap(
                of(
                        definedBy(0, 0, 1), mock(CellPopulationDescription.class),
                        commonInternalPos, firstMapCommonCellPopulationDescription
                )
        );

        final PopulationDistributionMap secondMap = PopulationDistributionMap.distributionFromMap(
                of(
                        commonInternalPos, secondMapCommonCellPopulationDescription,
                        definedBy(4, 0, 4), mock(CellPopulationDescription.class)
                )
        );


        // when

        PopulationDistributionMap mergedMap = firstMap.merge(secondMap);

        // then

        assertThat(mergedMap.getPopulationFor(commonInternalPos)).isSameAs(expectedMergedCellPopulationDescription);

    }

    @Test
    public void shouldReturnEmptyListOfInternalPositionsForEmptyMap() throws Exception {

        // given

        final PopulationDistributionMap tested = emptyDistributionMap();

        // when

        Set<InternalPosition> internalPositions = tested.getInternalPositions();

        // then

        assertThat(internalPositions).isEmpty();

    }


    @Test
    public void shouldReturnInternalPositionsForMap() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(2, 0, 0);
        final InternalPosition pos3 = definedBy(4, 0, 4);

        final PopulationDistributionMap testedMap = PopulationDistributionMap.distributionFromMap(
            of(
                pos1, mock(CellPopulationDescription.class),
                pos2, mock(CellPopulationDescription.class),
                pos3, mock(CellPopulationDescription.class)
            )
        );


        // when

        Set<InternalPosition> internalPositions = testedMap.getInternalPositions();

        // then

        assertThat(internalPositions).containsOnly(pos1, pos2, pos3);


    }

    @Test
    public void shouldReturnEmptyCellPopulationForInternalPositionNotFromMap() throws Exception {

        // given

        final PopulationDistributionMap tested = PopulationDistributionMap.distributionFromMap(
                of(
                        definedBy(1, 2, 3), mock(CellPopulationDescription.class),
                        definedBy(1, 1, 3), mock(CellPopulationDescription.class)
                )
        );

        // when

        CellPopulationDescription populationDescription = tested.getPopulationFor(definedBy(0, 0, 0));

        // then

        assertThat(populationDescription).isSameAs(CellPopulationDescription.emptyPopulation());

    }

    @Test
    public void shouldReturnCellPopulationForInternalPosition() throws Exception {

        // given

        final InternalPosition pos1 = definedBy(0, 0, 1);
        final InternalPosition pos2 = definedBy(2, 0, 0);
        final InternalPosition pos3 = definedBy(4, 0, 4);

        final CellPopulationDescription pos1Population = mock(CellPopulationDescription.class);
        final CellPopulationDescription pos2Population = mock(CellPopulationDescription.class);
        final CellPopulationDescription pos3Population = mock(CellPopulationDescription.class);

        final PopulationDistributionMap testedMap = PopulationDistributionMap.distributionFromMap(
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
}