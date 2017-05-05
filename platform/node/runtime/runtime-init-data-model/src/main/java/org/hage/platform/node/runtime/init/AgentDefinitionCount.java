package org.hage.platform.node.runtime.init;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgentDefinitionCount implements Serializable {
    private final AgentDefinition agentDefinition;
    private final int count;
}
