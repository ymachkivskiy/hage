package org.hage.platform.component.runtime.unit.context;

import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.hage.platform.simulation.runtime.control.AddressedAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.Collections;
import java.util.List;

public class ControlContextAdapter extends CommonContextAdapter implements ControlAgentManageContext {


    private final UnitPopulationController unitPopulationController;

    public ControlContextAdapter(UnitLocationContext unitLocationContext, UnitAgentCreationContext unitAgentCreationContext, UnitPopulationController unitPopulationController) {
        super(unitLocationContext, unitAgentCreationContext);
        this.unitPopulationController = unitPopulationController;
    }

    @Override
    public <T extends Agent> List<AddressedAgent<T>> queryAgentsOfType(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }

    @Override
    public boolean killAgent(AgentAddress agentAddress) {
        return agentAddress instanceof AgentAdapter && unitPopulationController.removeWithKilling(((AgentAdapter) agentAddress));
    }

    @Override
    public List<AgentAddress> queryLocalAgentsAddresses() {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }


    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
