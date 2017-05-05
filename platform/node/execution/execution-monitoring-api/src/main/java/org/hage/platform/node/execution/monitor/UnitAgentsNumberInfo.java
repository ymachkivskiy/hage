package org.hage.platform.node.execution.monitor;

import lombok.Data;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;

@Data
public class UnitAgentsNumberInfo implements Serializable {
    private final Position unitPosition;
    private final AgentsInfo agentsInfo;
}
