package org.hage.platform.config.load.adapter.generate.select;

import org.hage.platform.config.load.definition.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.Dimensions;
import org.hage.platform.habitat.structure.InternalPosition;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class AllPositionsSelectorTest {

    private AllPositionsSelector tested;

    @Before
    public void setUp() throws Exception {
        tested = new AllPositionsSelector();
    }

    @Test
    public void shouldReturnAllPositionsForChunk() throws Exception {

        // given

        final Chunk chunk = new Chunk(InternalPosition.definedBy(1, 2, 4), Dimensions.of(3, 4, 6));

        // when

        Set<InternalPosition> selectedPositions = tested.select(chunk, PositionsSelectionData.allPositions());

        // then

        assertThat(selectedPositions).isEqualTo(chunk.getInternalPositions());

    }


}