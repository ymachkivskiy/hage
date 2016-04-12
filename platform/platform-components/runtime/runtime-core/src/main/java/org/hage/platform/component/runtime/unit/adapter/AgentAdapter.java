package org.hage.platform.component.runtime.unit.adapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.Agent;
import org.hage.platform.simulation.runtime.AgentAddress;
import org.hage.platform.simulation.runtime.Context;

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
        return this;
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
    public Neighbors querySurroundingUnits() {
        return null;
    }

    @Override
    public void notifyStopConditionSatisfied(){
    }

}
