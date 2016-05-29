package org.hage.platform.component.monitoring;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicStats implements Serializable {
    private final ExecutionTimeStats executionTimeStats;
    private final SummaryAgentsStats summaryAgentsStats;
}
