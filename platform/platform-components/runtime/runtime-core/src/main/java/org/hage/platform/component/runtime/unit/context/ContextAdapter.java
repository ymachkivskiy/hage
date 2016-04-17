package org.hage.platform.component.runtime.unit.context;

import lombok.Setter;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitPopulationModificationContext;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import java.util.Collections;
import java.util.Set;

public class ContextAdapter extends CommonContextAdapter implements AgentManageContext {

    @Setter
    private AgentAdapter agentAdapter;

    public ContextAdapter(UnitLocationContext unitLocationContext, UnitPopulationModificationContext unitPopulationModificationContext) {
        super(unitLocationContext, unitPopulationModificationContext);
    }

    @Override
    public AgentAddress queryAddress() {
        return agentAdapter;
    }

    @Override
    public Set<AgentAddress> queryOtherLocalAgents() {
        //todo : NOT IMPLEMENTED
        return Collections.emptySet();
    }

    @Override
    public void die() {
        //todo : NOT IMPLEMENTED

    }

    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
