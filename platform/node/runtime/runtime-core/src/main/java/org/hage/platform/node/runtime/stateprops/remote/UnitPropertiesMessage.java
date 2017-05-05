package org.hage.platform.node.runtime.stateprops.remote;

import lombok.Data;
import org.hage.platform.node.runtime.stateprops.registry.PositionUnitProperties;

import java.io.Serializable;
import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@Data
public class UnitPropertiesMessage implements Serializable {
    private final List<PositionUnitProperties> positionUnitProperties;

    public UnitPropertiesMessage(List<PositionUnitProperties> positionUnitProperties) {
        this.positionUnitProperties = nullSafe(positionUnitProperties);
    }
}
