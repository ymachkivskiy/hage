package org.hage.platform.component.monitoring;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;

@Data
public class UnitSpecificAgentsStats implements Serializable {
    private final Position unitPosition;
    private final int agentsNumber;
}
