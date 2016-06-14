package org.hage.platform.component.execution.monitor;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DynamicExecutionInfo implements Serializable {
    private final StepExecutionDurationInfo executionTimeStats;
    private final List<UnitSpecificAgentsStats> unitSpecificAgentsStats;
}
