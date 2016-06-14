package org.hage.platform.component.execution.monitor;

import lombok.Data;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;

// TODO: rename
@Data
public class UnitSpecificAgentsStats implements Serializable {
    private final Position unitPosition;
    private final int agentsNumber;
}
