package org.hage.platform.component.runtime.unitmove;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;

@Data
public class PackedUnit implements Serializable {
    private final Position position;
    private final UnitConfiguration configuration;
}
