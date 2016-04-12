package org.hage.platform.component.runtime.unit.contextadapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.control.AddressedAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ControlAgentAdapter implements ControlAgentManageContext {
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(this);
    }



    @Override
    public UnitAddress queryLocalUnit() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public Neighbors querySurroundingUnits() {
        //todo : NOT IMPLEMENTED
        return null;
    }




    @Override
    public <T extends Agent> List<AddressedAgent<T>> queryAgentsOfType(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public boolean killAgent(AgentAddress agentAddress) {
        //todo : NOT IMPLEMENTED
        return false;
    }

    @Override
    public List<AgentAddress> queryLocalAgentsAddresses() {
        //todo : NOT IMPLEMENTED
        return null;
    }




    @Override
    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED

    }

    @Override
    public <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED

    }




    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
