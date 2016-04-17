package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.unit.context.AgentLocalEnvironment;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;

@RequiredArgsConstructor
public class AgentAdapter implements AgentAddress {

    private final AgentLocalEnvironment environment;
    private final long address;
    private final Agent agent;

    public void performStep() {
        agent.step(environment.contextForAgent(this));
    }

    @Override
    public String getUniqueIdentifier() {
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
