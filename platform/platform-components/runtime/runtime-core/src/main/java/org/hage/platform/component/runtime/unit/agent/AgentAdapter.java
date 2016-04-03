package org.hage.platform.component.runtime.unit.agent;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.runtime.Agent;
import org.hage.platform.simulation.runtime.AgentAddress;
import org.hage.platform.simulation.runtime.Context;
import org.hage.platform.simulation.runtime.UnitAddress;

import java.util.Set;

@RequiredArgsConstructor
public class AgentAdapter implements AgentAddress, Context {

    private final int id;
    private final String friendlyName;
    private final Agent agent;

    public void performStep() {
        agent.step(this);
    }

    @Override
    public String getUniqueIdentifier() {
        // TODO: impl
        return null;
    }

    @Override
    public String getFriendlyName() {
        // TODO: impl
        return friendlyName;
    }

    @Override
    public String getAgentFriendlyName() {
        return friendlyName; // TODO: impl
    }

    @Override
    public AgentAddress queryAddress() {
        return null;
    }

    @Override
    public Set<AgentAddress> queryLocalAgents() {
        return null;
    }

    @Override
    public UnitAddress queryUnit() {
        return null;
    }

    @Override
    public Set<UnitAddress> querySurroundingUnits() {
        return null;
    }

    @Override
    public void notifyStopConditionSatisfied() {

    }

}
