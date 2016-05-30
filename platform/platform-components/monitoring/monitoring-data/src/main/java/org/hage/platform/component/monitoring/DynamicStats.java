package org.hage.platform.component.monitoring;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DynamicStats implements Serializable {
    private final ExecutionTimeStats executionTimeStats;
    private final List<UnitSpecificAgentsStats> unitSpecificAgentsStats;
}
