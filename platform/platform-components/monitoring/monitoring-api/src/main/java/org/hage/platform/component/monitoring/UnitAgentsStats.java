package org.hage.platform.component.monitoring;

import lombok.Data;
import org.hage.platform.component.structure.Position;

@Data
public class UnitAgentsStats {
    private final Position unitPosition;
    private final int agentsNumber;
}
