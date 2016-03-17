package org.hage.platform.component.runtime.agent;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.base.Agent;
import org.hage.platform.simulation.base.Context;
import org.hage.platform.simulation.identification.AgentAddress;
import org.hage.platform.simulation.identification.UnitAddress;

import java.io.Serializable;
import java.util.Set;

@RequiredArgsConstructor
public class AgentAdapter implements AgentAddress, Serializable {

    private final int id;
    private final String friendlyName;
    private final Agent agent;

    public void performStep() {
        // TODO: pass context
        agent.step(new DummyCtxt());
    }

    @Override
    public String getUniqueIdentifier() {
        // TODO: impl
        return null;
    }

    @Override
    public String getFriendlyName() {
        // TODO: impl
        return null;
    }

    private static class DummyCtxt implements Context {

        @Override
        public String getAgentFriendlyName() {
            return "dummmy name";
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
}
