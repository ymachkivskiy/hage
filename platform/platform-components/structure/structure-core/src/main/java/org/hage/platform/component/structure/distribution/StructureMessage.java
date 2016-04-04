package org.hage.platform.component.structure.distribution;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;
import java.util.Collection;

@Data
class StructureMessage implements Serializable {
    private final Collection<Position> activatedPositions;
    private final Collection<Position> deactivatedPositions;
}
