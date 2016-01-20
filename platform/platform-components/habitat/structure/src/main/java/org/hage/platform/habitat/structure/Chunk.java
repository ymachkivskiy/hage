package org.hage.platform.habitat.structure;

import lombok.Data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Collections.emptySet;

@Data
public final class Chunk {
    private final InternalPosition startPos;
    private final Dimensions dimensions;

    public Long getSize() {
        return ((long) dimensions.getXDim()) * dimensions.getYDim() * dimensions.getZDim();
    }

    public boolean containsPosition(InternalPosition cellPosition) {
        InternalPosition xExtreme = startPos.shift(dimensions.getXDim() - 1, 0, 0);
        InternalPosition yExtreme = startPos.shift(0, dimensions.getYDim() - 1, 0);
        InternalPosition zExtreme = startPos.shift(0, 0, dimensions.getZDim() - 1);

        return cellPosition.getZPos() >= xExtreme.getZPos()
            && cellPosition.getZPos() <= zExtreme.getZPos()
            && cellPosition.getXPos() >= yExtreme.getXPos()
            && cellPosition.getXPos() <= xExtreme.getXPos()
            && cellPosition.getYPos() >= zExtreme.getYPos()
            && cellPosition.getYPos() <= yExtreme.getYPos();
    }


    public Set<InternalPosition> getInternalPositions() {
        Set<InternalPosition> internalPositions = new HashSet<>();

        for (int xPos = startPos.getXPos(), i = 0; i < dimensions.getXDim(); xPos++, i++) {
            for (int yPos = startPos.getYPos(), j = 0; j < dimensions.getYDim(); yPos++, j++) {
                for (int zPos = startPos.getZPos(), k = 0; k < dimensions.getZDim(); zPos++, k++) {
                    internalPositions.add(InternalPosition.definedBy(xPos, yPos, zPos));
                }
            }
        }

        return internalPositions;
    }

    public Set<InternalPosition> getRandomPositions(Long count) {

        if (count == 0) {
            return emptySet();
        }

        if (count >= getSize()) {
            return getInternalPositions();
        }

        Set<InternalPosition> randomPositions = new HashSet<>();

        Set<Long> indexesToInclude = generateIndexes(count);

        long idxCounter = 0;
        for (int xPos = startPos.getXPos(), i = 0; i < dimensions.getXDim(); xPos++, i++) {
            for (int yPos = startPos.getYPos(), j = 0; j < dimensions.getYDim(); yPos++, j++) {
                for (int zPos = startPos.getZPos(), k = 0; k < dimensions.getZDim(); zPos++, k++, ++idxCounter) {
                    if (indexesToInclude.contains(idxCounter)) {
                        randomPositions.add(InternalPosition.definedBy(xPos, yPos, zPos));
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
