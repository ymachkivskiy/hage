package org.hage.platform.config.local.gen.select;

import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

class RandomSelectionRandomNumberSelector implements PositionsSelector {

    private Random rand = new Random();

    @Override
    public Set<InternalPosition> select(Chunk chunk, PositionsSelectionData selectionData) {
        long chosenNumberOfPositions = Math.floorMod(abs(rand.nextLong()), chunk.getSize());
        return chunk.getRandomPositions(chosenNumberOfPositions);
    }
}
