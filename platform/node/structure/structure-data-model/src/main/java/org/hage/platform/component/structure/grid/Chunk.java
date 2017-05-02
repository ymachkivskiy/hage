package org.hage.platform.component.structure.grid;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.hage.platform.component.structure.Position.position;

@Data
public final class Chunk {
    private final Position startPos;
    private final Dimensions dimensions;

    public int getSize() {
        return dimensions.horizontalSize * dimensions.depthSize * dimensions.verticalSize;
    }

    public boolean containsPosition(Position cellPosition) {
        Position xExtreme = startPos.shift(0, dimensions.horizontalSize - 1, 0);
        Position yExtreme = startPos.shift(dimensions.depthSize - 1, 0, 0);
        Position zExtreme = startPos.shift(0, 0, dimensions.verticalSize - 1);

        return cellPosition.vertical >= xExtreme.vertical
            && cellPosition.vertical <= zExtreme.vertical
            && cellPosition.horizontal >= yExtreme.horizontal
            && cellPosition.horizontal <= xExtreme.horizontal
            && cellPosition.depth >= zExtreme.depth
            && cellPosition.depth <= yExtreme.depth;
    }


    public Set<Position> getInternalPositions() {
        Set<Position> positions = new HashSet<>();

        for (int xPos = startPos.horizontal, i = 0; i < dimensions.horizontalSize; xPos++, i++) {
            for (int yPos = startPos.depth, j = 0; j < dimensions.depthSize; yPos++, j++) {
                for (int zPos = startPos.vertical, k = 0; k < dimensions.verticalSize; zPos++, k++) {
                    positions.add(position(yPos, xPos, zPos));
                }
            }
        }

        return positions;
    }

    public Set<Position> getRandomPositions(int count) {

        if (count == 0) {
            return emptySet();
        }

        if (count >= getSize()) {
            return getInternalPositions();
        }

        Set<Position> randomPositions = new HashSet<>();

        Set<Long> indexesToInclude = generateIndexes(count);

        long idxCounter = 0;
        for (int xPos = startPos.horizontal, i = 0; i < dimensions.horizontalSize; xPos++, i++) {
            for (int yPos = startPos.depth, j = 0; j < dimensions.depthSize; yPos++, j++) {
                for (int zPos = startPos.vertical, k = 0; k < dimensions.verticalSize; zPos++, k++, ++idxCounter) {
                    if (indexesToInclude.contains(idxCounter)) {
                        randomPositions.add(position(yPos, xPos, zPos));
                    }
                }
            }
        }

        return randomPositions;
    }

    private Set<Long> generateIndexes(int count) {
        Random rand = new Random();
        final int size = getSize();

        Set<Long> idxes = new HashSet<>();

        while (idxes.size() != count) {
            long randomLong = rand.nextLong();
            idxes.add(Math.floorMod(randomLong, size));
        }

        return idxes;
    }

}
