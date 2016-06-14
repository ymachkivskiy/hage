package org.hage.platform.component.execution.monitor;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

// TODO: rework
@Data
public class ExecutionTimeStats implements Serializable {
    private final Duration summaryStepDuration;
    private final Duration unitPropertiesUpdateTime;
    private final Duration controlAgentsDuration;
    private final Duration agentsTime;
}
