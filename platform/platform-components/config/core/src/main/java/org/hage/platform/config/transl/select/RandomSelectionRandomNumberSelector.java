package org.hage.platform.config.transl.select;

import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;

class RandomSelectionRandomNumberSelector implements PositionsSelector {

    private Random rand = new Random();

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        long chosenNumberOfPositions = floorMod(abs(rand.nextLong()), chunk.getSize());
        return chunk.getRandomPositions(chosenNumberOfPositions);
    }
}
