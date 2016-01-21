package org.hage.platform.config.local.util;

import org.fest.assertions.Condition;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;

import java.util.Collection;

public class ChunkPositionsConditions {

    public static Condition<Collection<?>> ALL_POSITIONS_BELONGS_TO_CHUNK(Chunk chunk) {
        return new Condition<Collection<?>>() {
            @Override
            public boolean matches(Collection<?> objects) {
                Collection<InternalPosition> internalPositions = (Collection<InternalPosition>) objects;

                for (InternalPosition internalPosition : internalPositions) {
                    if (!chunk.containsPosition(internalPosition)) return false;
                }

                return true;
            }
        };
    }

}
