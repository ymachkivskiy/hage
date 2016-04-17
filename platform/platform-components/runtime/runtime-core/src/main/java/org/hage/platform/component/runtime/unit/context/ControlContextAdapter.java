package org.hage.platform.component.runtime.unit.context;

import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationModificationContext;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.hage.platform.simulation.runtime.control.AddressedAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.List;

public class ControlContextAdapter extends CommonContextAdapter implements ControlAgentManageContext {


    public ControlContextAdapter(UnitLocationContext unitLocationContext, UnitPopulationModificationContext unitPopulationModificationContext) {
        super(unitLocationContext, unitPopulationModificationContext);
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
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
