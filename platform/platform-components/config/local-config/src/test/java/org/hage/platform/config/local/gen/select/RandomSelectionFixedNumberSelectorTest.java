package org.hage.platform.config.local.gen.select;

import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.Dimensions;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.PositionsSelectionData.randomPositions;
import static org.hage.platform.config.local.util.ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK;

public class RandomSelectionFixedNumberSelectorTest {

    private RandomSelectionFixedNumberSelector tested;

    @Before
    public void setUp() throws Exception {
        tested = new RandomSelectionFixedNumberSelector();
    }

    @Test
    public void shouldReturnNoPositionsWhenFixedNumberIsZero() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(32, 14, 71), Dimensions.of(3, 41, 366));

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, randomPositions(0));

        // then

        assertThat(selectedPositions).isEmpty();


    }

    @Test
    public void shouldReturnAllInternalPositionsWhenFixedNumberIsEqualToChunkSize() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(32, 4, 71), Dimensions.of(23, 4, 366));

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, randomPositions(chunk.getSize()));

        // then

        assertThat(selectedPositions).isEqualTo(chunk.getInternalPositions());


    }

    @Test
    public void shouldReturnAllInternalPositionsWhenFixedNumberIsGreaterThanChunkSize() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(32, 4, 71), Dimensions.of(23, 4, 366));

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, randomPositions(chunk.getSize() + 100));

        // then

        assertThat(selectedPositions).isEqualTo(chunk.getInternalPositions());


    }

    @Test
    public void shouldReturnRandomFixedNumberOfInternalPositionsFromChunk() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(322, 41, 7), Dimensions.of(23, 4, 16));
        final int numberOfPositions = 57;

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, randomPositions(numberOfPositions));

        // then

        assertThat(selectedPositions).hasSize(numberOfPositions);
        assertThat(selectedPositions).satisfies(ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));

    }
}