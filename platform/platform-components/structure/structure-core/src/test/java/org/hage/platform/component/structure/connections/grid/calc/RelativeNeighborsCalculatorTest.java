package org.hage.platform.component.structure.connections.grid.calc;

import org.hage.platform.component.structure.Position;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.component.structure.Position.position;
import static org.hage.platform.component.structure.connections.RelativePosition.*;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.*;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.MOORE_NEIGHBORHOOD;
import static org.hage.platform.component.structure.grid.GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

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


    //region Depth Torus boundary condition

    @Test
    public void testDepthTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 1), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 3, 1));
    }


    @Test
    public void testDepthTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 3, 2), ON_RIGHT);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testDepthTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 2), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 0, 1),
            position(3, 0, 2),
            position(3, 1, 1),
            position(3, 1, 2)
        );
    }


    @Test
    public void testDepthTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 2), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 1, 1),
            position(0, 1, 2),
            position(1, 1, 1),
            position(1, 1, 2),
            position(3, 1, 1),
            position(3, 1, 2)
        );
    }


    //endregion


    //region Horizontal Torus boundary condition

    @Test
    public void testHorizontalTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 0, 1), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(position(2, 3, 1));
    }


    @Test
    public void testHorizontalTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 1, 2), ABOVE);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testHorizontalTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 1), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 0, 0),
            position(3, 0, 1),
            position(3, 0, 2),
            position(2, 0, 0),
            position(2, 0, 1),
            position(2, 0, 2)
        );
    }


    @Test
    public void testHorizontalTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 3, 2), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 3, 2),
            position(0, 3, 1),
            position(0, 2, 1),
            position(0, 2, 2),
            position(0, 0, 1),
            position(0, 0, 2)
        );
    }


    //endregion


    //region Vertical Torus boundary condition

    @Test
    public void testVerticalTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 1, 2), ABOVE);

        // then
        assertThat(neighbors).containsOnly(position(3, 1, 0));
    }


    @Test
    public void testVerticalTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 1, 2), IN_FRONT);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testVerticalTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 2, 0), BELOW);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 1, 2),
            position(3, 2, 2),
            position(3, 3, 2),
            position(2, 1, 2),
            position(2, 2, 2),
            position(2, 3, 2)
        );
    }


    @Test
    public void testVerticalTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 2), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 2, 2),
            position(3, 2, 1),
            position(3, 2, 0),
            position(2, 2, 2),
            position(2, 2, 1),
            position(2, 2, 0)
        );
    }

    //endregion


    //region Depth and Horizontal Torus boundary condition

    @Test
    public void testDepthAndHorizontalTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_HORIZONTAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 0), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(position(3, 0, 0));
    }


    @Test
    public void testDepthAndHorizontalTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_HORIZONTAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(1, 2, 0), BELOW);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testDepthAndHorizontalTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_HORIZONTAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 3, 2),
            position(0, 3, 1),
            position(0, 2, 2),
            position(0, 2, 1),
            position(0, 0, 2),
            position(0, 0, 1)
        );
    }


    @Test
    public void testDepthAndHorizontalTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_HORIZONTAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 0, 2), BELOW);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 0, 1),
            position(3, 1, 1),
            position(3, 3, 1),
            position(2, 0, 1),
            position(2, 1, 1),
            position(2, 3, 1),
            position(0, 0, 1),
            position(0, 1, 1),
            position(0, 3, 1)
        );
    }

    //endregion


    //region Depth and Vertical Torus boundary condition

    @Test
    public void testDepthAndVerticalTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 3, 2));
    }


    @Test
    public void testDepthAndVerticalTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 3, 2), ON_RIGHT);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testDepthAndVerticalTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(
            position(0, 3, 2),
            position(0, 3, 1),
            position(0, 3, 0),
            position(0, 2, 2),
            position(0, 2, 1),
            position(0, 2, 0)
        );
    }


    @Test
    public void testDepthAndVerticalTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(DEPTH_AND_VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 2, 0), ON_RIGHT);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 3, 0),
            position(3, 3, 1),
            position(3, 3, 2),
            position(2, 3, 0),
            position(2, 3, 1),
            position(2, 3, 2),
            position(0, 3, 0),
            position(0, 3, 1),
            position(0, 3, 2)
        );
    }

    //endregion


    //region Horizontal adn Vertical Torus boundary condition


    @Test
    public void testHorizontalAndVerticalTorusOnGridSideVonNeumanNeighborsAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL_AND_VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 0, 2), ABOVE);

        // then
        assertThat(neighbors).containsOnly(position(3, 0, 0));
    }


    @Test
    public void testHorizontalAndVerticalTorusOnGridSideVonNeumanNeighborsNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL_AND_VERTICAL__TORUS, definedBy(4, 4, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 1, 2), IN_FRONT);

        // then
        assertThat(neighbors).isEmpty();
    }

    @Test
    public void testHorizontalAndVerticalTorusMooreNeighborsShiftAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL_AND_VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 0, 2), ON_LEFT);

        // then
        assertThat(neighbors).containsOnly(
            position(3, 3, 0),
            position(3, 3, 1),
            position(3, 3, 2),
            position(2, 3, 0),
            position(2, 3, 1),
            position(2, 3, 2)
        );
    }


    @Test
    public void testHorizontalAndVerticalTorusMooreNeighborsShiftNotAlongTorus() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(HORIZONTAL_AND_VERTICAL__TORUS, definedBy(4, 4, 3), MOORE_NEIGHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(3, 3, 2), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(
            position(2, 2, 0),
            position(2, 2, 1),
            position(2, 2, 2),
            position(2, 3, 0),
            position(2, 3, 1),
            position(2, 3, 2),
            position(2, 0, 0),
            position(2, 0, 1),
            position(2, 0, 2)
        );
    }


    //endregion


    //region Full Torus boundary condition


    //region Grid side

    @Test
    public void testFullTorusOnGridSideVonNeumanNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 1, 1), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 1, 1));

    }

    @Test
    public void testFullTorusOnGridSideMooreNeighbors() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

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
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(0, 0, 1), AT_BACK);

        // then
        assertThat(neighbors).containsOnly(position(2, 0, 1));
    }

    @Test
    public void testFullTorusOnGridEdgeMooreNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

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
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

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
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), VON_NEUMANN_NEGIHBORHOOD);

        // when
        List<Position> neighbors = tested.neighborsOf(position(2, 0, 2), IN_FRONT);

        // then
        assertThat(neighbors).containsOnly(position(0, 0, 2));
    }

    @Test
    public void testFullTorusOnGridCornerMooreNeighborsAllMirrored() {
        // given
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

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
        RelativeNeighborsCalculator tested = new RelativeNeighborsCalculator(FULL__TORUS, definedBy(3, 3, 3), MOORE_NEIGHBORHOOD);

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