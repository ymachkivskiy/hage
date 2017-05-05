package org.hage.platform.node.runtime.activepopulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.node.runtime.unit.AgentExecutionContextEnvironment;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;

@RequiredArgsConstructor
public class AgentAdapter implements AgentAddress {

    @Getter
    private final Agent agent;
    private final long address;
    private final AgentExecutionContextEnvironment environment;

    public void performStep() {
        agent.step(environment.contextForAgent(this));
    }

    @Override
    public String getFriendlyIdentifier() {
        return agent.getClass().getSimpleName() + "(" + address + ")@" + environment.getUniqueIdentifier();
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
        return agent.getClass().getSimpleName() + "(" + address + ")";
    }
}
