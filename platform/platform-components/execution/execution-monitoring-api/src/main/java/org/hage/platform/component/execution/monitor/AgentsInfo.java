package org.hage.platform.component.execution.monitor;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgentsInfo implements Serializable {
    private final int activeAgentsNumber;
    private final int agentsToAddNumber;
    private final int agentsToRemoveNumber;
}
