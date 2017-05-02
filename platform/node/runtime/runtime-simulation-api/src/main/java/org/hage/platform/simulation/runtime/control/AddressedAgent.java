package org.hage.platform.simulation.runtime.control;

import lombok.Data;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;

@Data
public class AddressedAgent<T extends Agent> {
    private final T agent;
    private final AgentAddress address;
}
