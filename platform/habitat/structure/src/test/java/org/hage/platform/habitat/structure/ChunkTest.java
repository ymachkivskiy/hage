package org.hage.platform.habitat.structure;

import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChunkTest {


    @Test
    public void shouldContainPosition() throws Exception {

        // given

        final Chunk tested = new Chunk(
            InternalPosition.definedBy(0, 2, 0), Dimensions.of(5, 5, 5)
        );

        // when

        // then

        assertTrue(tested.containsPosition(InternalPosition.definedBy(0, 2, 0)));
        assertTrue(tested.containsPosition(InternalPosition.definedBy(3, 6, 1)));
        assertTrue(tested.containsPosition(InternalPosition.definedBy(4, 6, 4)));

    }

    @Test
    public void shouldReturnChunkSize() throws Exception {

        // given

        final Chunk tested = new Chunk(
            InternalPosition.definedBy(1, 2, 3), Dimensions.of(7, 2, 100)
        );

        // when

        Long actualChunkSize = tested.getSize();

        // then

        assertThat(actualChunkSize).isEqualTo(1400);

    }

    @Test
    public void shouldNotContainPosition() throws Exception {

        // given

        final Chunk tested = new Chunk(
            InternalPosition.definedBy(0, 2, 0), Dimensions.of(5, 5, 5)
        );

        // when

        // then

        assertFalse(tested.containsPosition(InternalPosition.definedBy(0, 0, 0)));
        assertFalse(tested.containsPosition(InternalPosition.definedBy(5, 7, 5)));
        assertFalse(tested.containsPosition(InternalPosition.definedBy(0, 2, 5)));
    }

    @Test
    public void shouldReturnInternalPositions() throws Exception {

        // given

        final InternalPosition pos__0_1_2 = InternalPosition.definedBy(0, 1, 2);
        final InternalPosition pos__1_1_2 = InternalPosition.definedBy(1, 1, 2);
        final InternalPosition pos__0_1_3 = InternalPosition.definedBy(0, 1, 3);
        final InternalPosition pos__1_1_3 = InternalPosition.definedBy(1, 1, 3);
        final InternalPosition pos__0_1_4 = InternalPosition.definedBy(0, 1, 4);
        final InternalPosition pos__1_1_4 = InternalPosition.definedBy(1, 1, 4);
        final InternalPosition pos__0_2_2 = InternalPosition.definedBy(0, 2, 2);
        final InternalPosition pos__1_2_2 = InternalPosition.definedBy(1, 2, 2);
        final InternalPosition pos__0_2_3 = InternalPosition.definedBy(0, 2, 3);
        final InternalPosition pos__1_2_3 = InternalPosition.definedBy(1, 2, 3);
        final InternalPosition pos__0_2_4 = InternalPosition.definedBy(0, 2, 4);
        final InternalPosition pos__1_2_4 = InternalPosition.definedBy(1, 2, 4);


        final Chunk tested = new Chunk(
            pos__0_1_2, Dimensions.of(2, 2, 3)
        );

        // when

        Set<InternalPosition> actualInternalPositions = tested.getInternalPositions();

        // then

        assertThat(actualInternalPositions).containsOnly(
            pos__0_1_2,
            pos__1_1_2,
            pos__0_1_3,
            pos__1_1_3,
            pos__0_1_4,
            pos__1_1_4,
            pos__0_2_2,
            pos__1_2_2,
            pos__0_2_3,
            pos__1_2_3,
            pos__0_2_4,
            pos__1_2_4
        );

    }

    @Test
    public void shouldReturnAllPositionsWhenCountIsGreaterEqualThanInternalPositionsCount() throws Exception {

        // given

        final InternalPosition pos__0_0_0 = InternalPosition.definedBy(0, 0, 0);
        final InternalPosition pos__0_0_1 = InternalPosition.definedBy(0, 0, 1);
        final InternalPosition pos__0_1_0 = InternalPosition.definedBy(0, 1, 0);
        final InternalPosition pos__0_1_1 = InternalPosition.definedBy(0, 1, 1);

        final Chunk tested = new Chunk(
            InternalPosition.definedBy(0, 0, 0), Dimensions.of(1, 2, 2)
        );

        // when

        Set<InternalPosition> randomPositions = tested.getRandomPositions(7L);

        // then

        assertThat(randomPositions).containsOnly(pos__0_0_0, pos__0_0_1, pos__0_1_0, pos__0_1_1);

    }

    @Test
    public void shouldReturnNoInternalPositionsWhenCountIsZero() throws Exception {

        // given

        final Chunk tested = new Chunk(
            InternalPosition.definedBy(1, 2, 3), Dimensions.of(32, 12, 66)
        );

        // when

        Set<InternalPosition> randomPositions = tested.getRandomPositions(0L);

        // then

        assertThat(randomPositions).isEmpty();


    }

    @Test
    public void shouldReturnFixedNumberOfRandomInternalPositions() throws Exception {

        // given

        final long positionsCount = 967;
        final Chunk tested = new Chunk(
            InternalPosition.definedBy(0, 2, 3), Dimensions.of(32, 12, 17)
        );

        // when

        Set<InternalPosition> randomInternalPositions = tested.getRandomPositions(positionsCount);

        // then

        assertThat(randomInternalPositions).hasSize((int) positionsCount);
        for (InternalPosition pos : randomInternalPositions) {
            assertTrue(tested.containsPosition(pos));
        }

    }
}