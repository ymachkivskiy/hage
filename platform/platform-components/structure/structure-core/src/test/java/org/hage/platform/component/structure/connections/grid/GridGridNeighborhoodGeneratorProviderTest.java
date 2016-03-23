package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Position;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.component.structure.connections.Position.position;
import static org.hage.platform.component.structure.connections.grid.GridNeighborhoodType.*;

public class GridGridNeighborhoodGeneratorProviderTest {


    private GridNeighborhoodGeneratorProvider tested = new GridNeighborhoodGeneratorProvider();

    @Test
    public void shouldReturnNoNeighbors() {
        // given
        // when
        List<Position> neighbors = tested.getNeighborhood(NO_NEIGHBORS).generateNeighbors(position(2, 1, 3));

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void shouldReturnNeighborsWithoutDiagonalNeighbors() {
        // given
        final int horizontal = 1;
        final int depth = 2;
        final int vertical = 3;

        // when
        List<Position> neighbors = tested.getNeighborhood(SIX_NEIGHBORS_ONLY_MUTUAL).generateNeighbors(position(depth, horizontal, vertical));

        // then
        assertThat(neighbors).doesNotHaveDuplicates();
        assertThat(neighbors).containsOnly(
            position(depth, horizontal - 1, vertical),
            position(depth, horizontal + 1, vertical),
            position(depth - 1, horizontal, vertical),
            position(depth + 1, horizontal, vertical),
            position(depth, horizontal, vertical - 1),
            position(depth, horizontal, vertical + 1)
        );
    }

    @Test
    public void shouldReturnNeighborsWithWithFirstGenerationOfDiagonalNeighbors() {
        // given
        final int horizontal = 1;
        final int depth = 2;
        final int vertical = 3;

        // when
        List<Position> neighbors = tested.getNeighborhood(EIGHTEEN_NEIGHBORS_CUBE_WITHOUT_CORNERS).generateNeighbors(position(depth, horizontal, vertical));

        // then
        assertThat(neighbors).doesNotHaveDuplicates();
        assertThat(neighbors).containsOnly(
            position(depth, horizontal - 1, vertical),
            position(depth, horizontal + 1, vertical),
            position(depth - 1, horizontal - 1, vertical),
            position(depth + 1, horizontal + 1, vertical),
            position(depth, horizontal - 1, vertical - 1),
            position(depth, horizontal + 1, vertical + 1),

            position(depth - 1, horizontal, vertical),
            position(depth + 1, horizontal, vertical),
            position(depth - 1, horizontal - 1, vertical),
            position(depth + 1, horizontal + 1, vertical),
            position(depth - 1, horizontal, vertical - 1),
            position(depth + 1, horizontal, vertical + 1),

            position(depth, horizontal, vertical - 1),
            position(depth, horizontal, vertical + 1),
            position(depth, horizontal - 1, vertical - 1),
            position(depth, horizontal + 1, vertical + 1),
            position(depth - 1, horizontal, vertical - 1),
            position(depth + 1, horizontal, vertical + 1)
        );

    }


    @Test
    public void shouldReturnFullThreeDimensionalCubeWithoutCentralPosition() {
        // given
        final int horizontal = 1;
        final int depth = 2;
        final int vertical = 3;

        // when
        List<Position> neighbors = tested.getNeighborhood(TWENTY_SIX_NEIGHBORS_FULL_CUBE).generateNeighbors(position(depth, horizontal, vertical));

        // then
        assertThat(neighbors).doesNotHaveDuplicates();
        assertThat(neighbors).containsOnly(
            position(depth, horizontal - 1, vertical),
            position(depth, horizontal + 1, vertical),
            position(depth - 1, horizontal - 1, vertical),
            position(depth + 1, horizontal + 1, vertical),
            position(depth, horizontal - 1, vertical - 1),
            position(depth, horizontal + 1, vertical + 1),

            position(depth - 1, horizontal, vertical),
            position(depth + 1, horizontal, vertical),
            position(depth - 1, horizontal - 1, vertical),
            position(depth + 1, horizontal + 1, vertical),
            position(depth - 1, horizontal, vertical - 1),
            position(depth + 1, horizontal, vertical + 1),

            position(depth, horizontal, vertical - 1),
            position(depth, horizontal, vertical + 1),
            position(depth, horizontal - 1, vertical - 1),
            position(depth, horizontal + 1, vertical + 1),
            position(depth - 1, horizontal, vertical - 1),
            position(depth + 1, horizontal, vertical + 1),

            position(depth - 1, horizontal - 1, vertical - 1),
            position(depth + 1, horizontal - 1, vertical + 1),
            position(depth + 1, horizontal - 1, vertical - 1),
            position(depth - 1, horizontal - 1, vertical + 1),
            position(depth - 1, horizontal + 1, vertical - 1),
            position(depth + 1, horizontal + 1, vertical - 1),
            position(depth - 1, horizontal + 1, vertical + 1),
            position(depth + 1, horizontal + 1, vertical + 1)

        );


    }
}