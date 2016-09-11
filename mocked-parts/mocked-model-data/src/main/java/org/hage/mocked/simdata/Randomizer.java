package org.hage.mocked.simdata;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.connections.UnitAddress;

import java.util.List;
import java.util.Random;

import static org.hage.util.CollectionUtils.nullSafe;

@Slf4j
public class Randomizer {

    private static final double randomWalkProbability = 0.3;


    private final Random random = new Random();

    public UnitAddress chooseRandom(List<UnitAddress> idealDesired, List<UnitAddress> allNeibs) {
        if (random.nextDouble() <= randomWalkProbability || idealDesired.isEmpty()) {
            return randomElement(allNeibs);
        } else {
            return randomElement(idealDesired);
        }

    }

    private UnitAddress randomElement(List<UnitAddress> list) {
        List<UnitAddress> safeList = nullSafe(list);

        if (safeList.isEmpty()) {
            log.error("Got empty list");
        }

        return safeList.get(random.nextInt(safeList.size()));
    }


}
