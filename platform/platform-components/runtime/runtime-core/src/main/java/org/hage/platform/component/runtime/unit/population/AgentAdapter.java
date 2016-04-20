package org.hage.platform.component.runtime.unit.population;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.control.AddressedAgent;

@RequiredArgsConstructor
public class AgentAdapter<T extends Agent> implements AgentAddress {

    @Getter
    private final Agent agent;
    private final long address;
    private final AgentLocalEnvironment environment;

    public void performStep() {
        agent.step(environment.contextForAgent(this));
    }

    @Override
    public String getFriendlyIdentifier() {
        return "agent(" + address + ")@" + environment.getUniqueIdentifier();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentAdapter that = (AgentAdapter) o;

        return address == that.address;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(address);
    }

    @Override
    public String toString() {
        return "agent(" + address + ")";
    }
}
