package org.hage.platform.config.def.agent;

import lombok.Data;

@Data
public final class AgentCountData {
    private final AgentCountMode agentCountMode;
    private final Integer value;
    private final Integer secondaryValue;
}
