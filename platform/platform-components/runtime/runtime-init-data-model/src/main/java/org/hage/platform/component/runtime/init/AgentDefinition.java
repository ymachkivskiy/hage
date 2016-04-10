package org.hage.platform.component.runtime.init;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.simulation.runtime.Agent;

@RequiredArgsConstructor
@ToString
@Getter
public class AgentDefinition {
    private final Class<? extends Agent> agentClass;
}
