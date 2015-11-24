package org.hage.platform.config.transl.select;

import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.Dimensions;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.Assertions.*;
import static org.hage.platform.config.def.agent.PositionsSelectionData.randomPositions;
import static org.hage.platform.config.util.ChunkPositionsConditions.ALL_POSITIONS_BELONGS_TO_CHUNK;
import static org.junit.Assert.assertTrue;

public class RandomSelectionRandomNumberSelectorTest {

    private RandomSelectionRandomNumberSelector tested;

    @Before
    public void setUp() throws Exception {
        tested = new RandomSelectionRandomNumberSelector();
    }

    @Test
    public void shouldSelectStartPositionOrNotSelectAnyPositionForUnitChunk() throws Exception {

        // given

        final Chunk unitChunk = new Chunk(InternalPosition.definedBy(14, 355, 1122), Dimensions.UNIT);

        // when

        Set<InternalPosition> selected = tested.select(unitChunk, randomPositions());

        // then

        assertTrue(selected.isEmpty() || (selected.contains(Dimensions.UNIT) && selected.size() == 1));

    }


    @Test
    public void shouldSelectRandomNumberOfRandomPositionsForBigChunk() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(23, 54, 11), Dimensions.of(34, 13, 112));

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, randomPositions());

        // then

        assertThat(selectedPositions).satisfies(ALL_POSITIONS_BELONGS_TO_CHUNK(chunk));


    }

}