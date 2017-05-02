package org.hage.platform.component.runtime.stateprops.registry;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;

@Data
public class PositionUnitProperties implements Serializable {
    private final Position position;
    private final UnitProperties properties;
}
