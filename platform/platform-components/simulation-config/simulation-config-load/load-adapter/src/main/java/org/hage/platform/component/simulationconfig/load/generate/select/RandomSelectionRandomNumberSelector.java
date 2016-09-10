package org.hage.platform.component.simulationconfig.load.generate.select;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

public class RandomSelectionRandomNumberSelector implements PositionsSelector {

    private Random rand = new Random();

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        int chosenNumberOfPositions = Math.floorMod(abs(rand.nextInt()), chunk.getSize());
        return chunk.getRandomPositions(chosenNumberOfPositions);
    }
}
