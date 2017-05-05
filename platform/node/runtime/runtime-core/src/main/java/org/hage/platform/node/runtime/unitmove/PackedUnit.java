package org.hage.platform.node.runtime.unitmove;

import lombok.Data;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;

@Data
public class PackedUnit implements Serializable {
    private final Position position;
    private final UnitConfiguration configuration;
}
