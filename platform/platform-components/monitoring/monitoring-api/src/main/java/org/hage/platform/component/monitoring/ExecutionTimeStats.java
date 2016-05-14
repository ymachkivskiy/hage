package org.hage.platform.component.monitoring;

import lombok.Data;

import java.time.Duration;

@Data
public class ExecutionTimeStats {
    private final Duration overallStepTime;
    private final Duration unitPropertiesUpdateTime;
    private final Duration controlAgentsTime;
    private final Duration agentsTime;
}
