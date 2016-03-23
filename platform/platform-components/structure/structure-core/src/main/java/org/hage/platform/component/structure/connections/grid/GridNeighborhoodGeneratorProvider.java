package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Position;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hage.platform.component.structure.connections.grid.GridNeighborhoodType.*;

//todo rewrite
public class GridNeighborhoodGeneratorProvider {

    private final EnumMap<GridNeighborhoodType, GridNeighborhoodGenerator> generators = new EnumMap<>(GridNeighborhoodType.class);

    public GridNeighborhoodGeneratorProvider() {
        generators.put(NO_NEIGHBORS, (p) -> emptyList());
        generators.put(SIX_NEIGHBORS_ONLY_MUTUAL, new SixNeighborsGridNeighborhoodGenerator());
        generators.put(EIGHTEEN_NEIGHBORS_CUBE_WITHOUT_CORNERS, new EighteenNeighborsGridNeighborhoodGenerator());
        generators.put(TWENTY_SIX_NEIGHBORS_FULL_CUBE, new TwentySixNeighborsGridNeighborhoodGenerator());
    }

    public GridNeighborhoodGenerator getNeighborhood(GridNeighborhoodType neighborhoodType) {
        return generators.get(neighborhoodType);
    }


    public static class SixNeighborsGridNeighborhoodGenerator implements GridNeighborhoodGenerator {

        protected final int shifts[] = {-1, 1};

        @Override
        public List<Position> generateNeighbors(Position center) {
            List<Position> result = new ArrayList<>(6);

            for (int shift : shifts) {
                result.add(center.shift(0, shift, 0));
                result.add(center.shift(shift, 0, 0));
                result.add(center.shift(0, 0, shift));
            }

            return result;
        }

    }


    public static class EighteenNeighborsGridNeighborhoodGenerator extends SixNeighborsGridNeighborhoodGenerator {
        @Override
        public List<Position> generateNeighbors(Position center) {
            List<Position> result = new ArrayList<>(18);

            result.addAll(super.generateNeighbors(center));

            for (int shift : shifts) {
                result.add(center.shift(shift, shift, 0));
                result.add(center.shift(0, shift, shift));
                result.add(center.shift(shift, 0, shift));
            }

            return result;

        }
    }


    public static class TwentySixNeighborsGridNeighborhoodGenerator extends EighteenNeighborsGridNeighborhoodGenerator {

        @Override
        public List<Position> generateNeighbors(Position center) {

            List<Position> result = new ArrayList<>(26);

            result.addAll(super.generateNeighbors(center));

            for (int shift : shifts) {
                result.add(center.shift(shift, shift, shift));
                result.add(center.shift(shift, -shift, shift));
                result.add(center.shift(-shift, shift, shift));
                result.add(center.shift(shift, shift, -shift));
            }

            return result;
        }
    }

}

