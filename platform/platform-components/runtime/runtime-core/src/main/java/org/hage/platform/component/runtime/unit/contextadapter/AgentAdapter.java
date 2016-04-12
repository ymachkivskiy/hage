package org.hage.platform.component.runtime.unit.contextadapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.Set;

@RequiredArgsConstructor
public class AgentAdapter implements AgentManageContext  {

    private final int id;
    private final Agent agent;

    public void performStep() {
        agent.step(this);
    }


    @Override
    public AgentAddress queryAddress() {
        //todo : NOT IMPLEMENTED
        return new AgentAddress() {
            @Override
            public String getUniqueIdentifier() {
                return "dummy";
            }
        };
    }



    @Override
    public Set<AgentAddress> queryOtherLocalAgents() {
        //todo : NOT IMPLEMENTED
        return null;
    }



    @Override
    public void die() {
        //todo : NOT IMPLEMENTED

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
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
