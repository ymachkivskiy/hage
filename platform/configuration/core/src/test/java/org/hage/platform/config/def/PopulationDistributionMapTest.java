package org.hage.platform.config.def;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.util.Collections.set;
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

        final PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        PopulationDistributionMap mergedMap = originalDistributionMap.merge(PopulationDistributionMap.empty());

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

        final PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
                        pos1, mock(CellPopulationDescription.class),
                        pos2, mock(CellPopulationDescription.class),
                        pos3, mock(CellPopulationDescription.class),
                        pos4, mock(CellPopulationDescription.class)
                )
        );

        // when

        PopulationDistributionMap mergedMap = PopulationDistributionMap.empty().merge(originalDistributionMap);

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

        final PopulationDistributionMap firstMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
                        pos1Map1, mock(CellPopulationDescription.class),
                        pos2Map1, mock(CellPopulationDescription.class)
                )
        );

        final PopulationDistributionMap secondMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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

        PopulationDistributionMap tested = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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

        final PopulationDistributionMap originalMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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

        final PopulationDistributionMap originalMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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
    public void shouldUpdatePopulationForCommonInternalPositionsDuringMergingMaps() throws Exception {

        // given

        final InternalPosition commonInternalPos = definedBy(2, 0, 0);

        final CellPopulationDescription firstMapCommonCellPopulationDescription = mock(CellPopulationDescription.class);
        final CellPopulationDescription secondMapCommonCellPopulationDescription = mock(CellPopulationDescription.class);
        final CellPopulationDescription expectedMergedCellPopulationDescription = mock(CellPopulationDescription.class);

        when(firstMapCommonCellPopulationDescription.merge(secondMapCommonCellPopulationDescription)).thenReturn(expectedMergedCellPopulationDescription);
        when(secondMapCommonCellPopulationDescription.merge(firstMapCommonCellPopulationDescription)).thenReturn(expectedMergedCellPopulationDescription);

        final PopulationDistributionMap firstMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
                        definedBy(0, 0, 1), mock(CellPopulationDescription.class),
                        commonInternalPos, firstMapCommonCellPopulationDescription
                )
        );

        final PopulationDistributionMap secondMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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

        final PopulationDistributionMap tested = PopulationDistributionMap.empty();

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

        final PopulationDistributionMap testedMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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

        final PopulationDistributionMap tested = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
                        definedBy(1, 2, 3), mock(CellPopulationDescription.class),
                        definedBy(1, 1, 3), mock(CellPopulationDescription.class)
                )
        );

        // when

        CellPopulationDescription populationDescription = tested.getPopulationFor(definedBy(0, 0, 0));

        // then

        assertThat(populationDescription).isSameAs(CellPopulationDescription.empty());

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

        final PopulationDistributionMap testedMap = PopulationDistributionMap.fromMap(
                ImmutableMap.of(
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