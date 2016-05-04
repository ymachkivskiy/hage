package org.hage.platform.component.runtime.stopcondition.agent;

import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hage.platform.component.runtime.stopcondition.agent.AgentType.AGENT;
import static org.hage.platform.component.runtime.stopcondition.agent.AgentType.CONTROL_AGENT;

@ToString(doNotUseGetters = true)
@Getter
public class AgentReport {
    private final Position position;
    private final AgentType agentType;
    private final Optional<Agent> reportingAgent;

    private AgentReport(Position position, AgentType agentType, Optional<Agent> reportingAgent) {
        this.position = checkNotNull(position);
        this.agentType = checkNotNull(agentType);
        this.reportingAgent = reportingAgent;
    }

    public static AgentReport reporterForControlAgentOf(Position position) {
        return new AgentReport(position, CONTROL_AGENT, empty());
    }

    public static AgentReport reporterForAgentIn(Position position, Agent agent) {
        return new AgentReport(position, AGENT, of(agent));
    }

}
