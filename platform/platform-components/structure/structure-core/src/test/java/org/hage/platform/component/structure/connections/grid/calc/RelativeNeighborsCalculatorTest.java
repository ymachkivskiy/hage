package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.Position;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.component.structure.Position.position;
import static org.hage.platform.component.structure.connections.RelativePosition.*;
import static org.hage.platform.component.structure.grid.BoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.grid.BoundaryConditions.FULL_TORUS;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.*;

public class RelativeNeighborsCalculatorTest {


    //region Closed boundary conditions

    @Test
    public void testClosedInsideGridVonNeumanNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 1), ABOVE);

        // then
        assertThat(neighbors).containsOnly(position(0, 0, 2));
    }

    @Test
    public void testClosedInsideGridMooreNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 0, 1), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 0),
            position(0, 1, 1),
            position(0, 1, 2),
            position(1, 1, 0),
            position(1, 1, 1),
            position(1, 1, 2),
            position(2, 1, 0),
            position(2, 1, 1),
            position(2, 1, 2)
        );
    }


    @Test
    public void testClosedOnGridSideVonNeumanNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 1, 1), AT_BACK);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testClosedOnGridCornerMooreNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 2, 2), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 1),
            position(0, 1, 2),
            position(0, 2, 1),
            position(0, 2, 2)
        );
    }


    //endregion


    //region Full Torus boundary condition


    //region Grid side

    @Test
    public void testFullTorusOnGridSideVonNeumanNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 1, 1), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 1, 1));

    }

    @Test
    public void testFullTorusOnGridSideMooreNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 0, 1), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 2, 0),
            position(0, 2, 1),
            position(0, 2, 2),
            position(1, 2, 0),
            position(1, 2, 1),
            position(1, 2, 2),
            position(2, 2, 0),
            position(2, 2, 1),
            position(2, 2, 2)
        );
    }

    //endregion


    //region Grid edge

    @Test
    public void testFullTorusOnGridEdgeVonNeumanNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 1), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(position(2, 0, 1));
    }

    @Test
    public void testFullTorusOnGridEdgeMooreNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 0, 0), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 2, 0),
            position(0, 2, 1),
            position(0, 2, 2),
            position(1, 2, 0),
            position(1, 2, 1),
            position(1, 2, 2),
            position(2, 2, 0),
            position(2, 2, 1),
            position(2, 2, 2)
        );
    }

    @Test
    public void testFullTorusOnGridEdgeMooreNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 1, 0), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 0, 0),
            position(1, 0, 0),
            position(1, 0, 1),
            position(0, 0, 1),
            position(2, 0, 0),
            position(2, 0, 1),
            position(0, 0, 2),
            position(1, 0, 2),
            position(2, 0, 2)
        );
    }

    //endregion


    //region Grid corner

    @Test
    public void testFullTorusOnGridCornerVonNeumanNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 0, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 0, 2));
    }

    @Test
    public void testFullTorusOnGridCornerMooreNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 2, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 2, 2),
            position(0, 1, 2),
            position(0, 1, 1),
            position(0, 2, 1),
            position(0, 0, 2),
            position(0, 0, 1),
            position(0, 0, 0),
            position(0, 1, 0),
            position(0, 2, 0)
        );
    }

    @Test
    public void testFullTorusOnGridCornerMooreNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 0), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 0),
            position(0, 1, 1),
            position(1, 1, 0),
            position(1, 1, 1),
            position(2, 1, 0),
            position(2, 1, 1),
            position(0, 1, 2),
            position(1, 1, 2),
            position(2, 1, 2)
        );
    }

    //endregion

    //endregion


}