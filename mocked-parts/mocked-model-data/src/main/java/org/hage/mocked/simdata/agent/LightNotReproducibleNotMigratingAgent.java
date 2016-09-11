package org.hage.mocked.simdata.agent;

import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import java.util.List;

public class LightNotReproducibleNotMigratingAgent implements Agent {


    @Override
    public void step(AgentManageContext context) {

        List<AgentAddress> localAgents = context.queryOtherLocalAgents();

        for (UnitAddress unitAddress : context.querySurroundingUnits().getAll()) {
            context.queryPropertiesOf(unitAddress).getValues();
        }

        for (int i = 0; i < 10; i++) {
            context.queryOtherLocalAgentsOfSameType();
        }

    }

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }
}
