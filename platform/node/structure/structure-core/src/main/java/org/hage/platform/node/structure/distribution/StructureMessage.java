package org.hage.platform.node.structure.distribution;

import lombok.Data;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;
import java.util.Collection;

@Data
class StructureMessage implements Serializable {
    private final Collection<Position> activatedPositions;
    private final Collection<Position> deactivatedPositions;
}
