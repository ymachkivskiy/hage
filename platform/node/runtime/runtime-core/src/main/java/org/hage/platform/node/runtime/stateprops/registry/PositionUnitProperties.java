package org.hage.platform.node.runtime.stateprops.registry;

import lombok.Data;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;

@Data
public class PositionUnitProperties implements Serializable {
    private final Position position;
    private final UnitProperties properties;
}
