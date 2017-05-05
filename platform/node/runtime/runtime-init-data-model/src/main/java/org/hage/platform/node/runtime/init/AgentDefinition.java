package org.hage.platform.node.runtime.init;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.io.Serializable;

@Data
public class AgentDefinition implements Serializable {
    private final Class<? extends Agent> agentClass;
}
