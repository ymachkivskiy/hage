package org.hage.platform.config.load.definition;

import lombok.Data;
import org.hage.platform.component.structure.definition.Dimensions;
import org.hage.platform.component.structure.definition.Position;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Collections.emptySet;

@Data
public final class Chunk {
    private final Position startPos;
    private final Dimensions dimensions;

    public Long getSize() {
        return ((long) dimensions.getXDim()) * dimensions.getYDim() * dimensions.getZDim();
    }

    public boolean containsPosition(Position cellPosition) {
        Position xExtreme = startPos.shift(dimensions.getXDim() - 1, 0, 0);
        Position yExtreme = startPos.shift(0, dimensions.getYDim() - 1, 0);
        Position zExtreme = startPos.shift(0, 0, dimensions.getZDim() - 1);

        return cellPosition.getVertical() >= xExtreme.getVertical()
            && cellPosition.getVertical() <= zExtreme.getVertical()
            && cellPosition.getHorizontal() >= yExtreme.getHorizontal()
            && cellPosition.getHorizontal() <= xExtreme.getHorizontal()
            && cellPosition.getDepth() >= zExtreme.getDepth()
            && cellPosition.getDepth() <= yExtreme.getDepth();
    }


    public Set<Position> getInternalPositions() {
        Set<Position> positions = new HashSet<>();

        for (int xPos = startPos.getHorizontal(), i = 0; i < dimensions.getXDim(); xPos++, i++) {
            for (int yPos = startPos.getDepth(), j = 0; j < dimensions.getYDim(); yPos++, j++) {
                for (int zPos = startPos.getVertical(), k = 0; k < dimensions.getZDim(); zPos++, k++) {
                    positions.add(Position.definedBy(xPos, yPos, zPos));
                }
            }
        }

        return positions;
    }

    public Set<Position> getRandomPositions(Long count) {

        if (count == 0) {
            return emptySet();
        }

        if (count >= getSize()) {
            return getInternalPositions();
        }

        Set<Position> randomPositions = new HashSet<>();

        Set<Long> indexesToInclude = generateIndexes(count);

        long idxCounter = 0;
        for (int xPos = startPos.getHorizontal(), i = 0; i < dimensions.getXDim(); xPos++, i++) {
            for (int yPos = startPos.getDepth(), j = 0; j < dimensions.getYDim(); yPos++, j++) {
                for (int zPos = startPos.getVertical(), k = 0; k < dimensions.getZDim(); zPos++, k++, ++idxCounter) {
                    if (indexesToInclude.contains(idxCounter)) {
                        randomPositions.add(Position.definedBy(xPos, yPos, zPos));
                    }
                }
            }
        }

        return randomPositions;
    }

    private Set<Long> generateIndexes(Long count) {
        Random rand = new Random();
        final Long size = getSize();

        Set<Long> idxes = new HashSet<>();

        while (idxes.size() != count) {
            long randomLong = rand.nextLong();
            idxes.add(Math.floorMod(randomLong, size));
        }

        return idxes;
    }

}
