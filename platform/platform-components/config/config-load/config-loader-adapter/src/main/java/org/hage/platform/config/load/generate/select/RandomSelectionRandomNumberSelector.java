package org.hage.platform.config.load.generate.select;

import org.hage.platform.component.structure.definition.Position;
import org.hage.platform.config.load.definition.Chunk;
import org.hage.platform.config.load.definition.agent.PositionsSelectionData;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

public class RandomSelectionRandomNumberSelector implements PositionsSelector {

    private Random rand = new Random();

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        long chosenNumberOfPositions = Math.floorMod(abs(rand.nextLong()), chunk.getSize());
        return chunk.getRandomPositions(chosenNumberOfPositions);
    }
}
