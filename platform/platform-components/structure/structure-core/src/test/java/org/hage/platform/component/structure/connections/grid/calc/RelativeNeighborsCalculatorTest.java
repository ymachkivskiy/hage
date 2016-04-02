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
    public void testClosedInsideGridFirstDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), FIRST_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 1), ABOVE);

        // then
        assertThat(neighbors).containsOnly(position(0, 0, 2));
    }

    @Test
    public void testClosedInsideGridSecondDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 1, 1), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(
            position(2, 1, 1),
            position(2, 0, 1),
            position(2, 2, 1),
            position(2, 1, 0),
            position(2, 1, 2)
        );
    }

    @Test
    public void testClosedInsideGridThirdDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testClosedOnGridSideFirstDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), FIRST_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 1, 1), AT_BACK);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testClosedOnGridEdgeSecondDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 2, 1), ABOVE);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 2),
            position(0, 2, 2),
            position(1, 2, 2)
        );
    }

    @Test
    public void testClosedOnGridCornerThirdDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(CLOSED, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testFullTorusOnGridSideFirstDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), FIRST_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 1, 1), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 1, 1));

    }

    @Test
    public void testFullTorusOnGridSideSecondDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 1, 2), ABOVE);

        // then
        assertThat(neighbors).containsOnly(
            position(1, 1, 0),
            position(0, 1, 0),
            position(2, 1, 0),
            position(1, 0, 0),
            position(1, 2, 0)
        );
    }

    @Test
    public void testFullTorusOnGridSideThirdDegreeNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testFullTorusOnGridEdgeFirstDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), FIRST_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 1), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(position(2, 0, 1));
    }

    @Test
    public void testFullTorusOnGridEdgeSecondDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 2, 0), BELOW);

        // then
        assertThat(neighbors).containsOnly(
            position(1, 2, 2),
            position(0, 2, 2),
            position(1, 1, 2),
            position(2, 2, 2),
            position(1, 0, 2)
        );
    }

    @Test
    public void testFullTorusOnGridEdgeSecondDegreeNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 2, 1), ABOVE);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 2, 2),
            position(0, 1, 2),
            position(1, 2, 2),
            position(2, 2, 2),
            position(0, 0, 2)
        );
    }

    @Test
    public void testFullTorusOnGridEdgeThirdDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testFullTorusOnGridEdgeThirdDegreeNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testFullTorusOnGridCornerFirstDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), FIRST_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 0, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 0, 2));
    }

    @Test
    public void testFullTorusOnGridCornerSecondDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 2, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 2, 2),
            position(0, 1, 2),
            position(0, 2, 1),
            position(0, 2, 0),
            position(0, 0, 2)
        );
    }

    @Test
    public void testFullTorusOnGridCornerSecondDegreeNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), SECOND_DEGREE);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 2, 2), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 2),
            position(0, 1, 1),
            position(1, 1, 2),
            position(0, 1, 0),
            position(2, 1, 2)
        );
    }

    @Test
    public void testFullTorusOnGridCornerThirdDegreeNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), THIRD_DEGREE);

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
    public void testFullTorusOnGridCornerThirdDegreeNeighborsMixedMirroring() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL_TORUS, definedBy(3, 3, 3), THIRD_DEGREE);

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