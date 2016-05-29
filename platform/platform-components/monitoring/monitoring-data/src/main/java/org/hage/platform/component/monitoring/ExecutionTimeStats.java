package org.hage.platform.component.monitoring;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

@Data
public class ExecutionTimeStats implements Serializable {
    private final Duration overallStepTime;
    private final Duration unitPropertiesUpdateTime;
    private final Duration controlAgentsTime;
    private final Duration agentsTime;
}
