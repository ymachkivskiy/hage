package org.hage.mocked.simdata.agent;

import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

public class LightReproducibleNotMigratingAgent implements Agent {

    private int energy = 10;

    @Override
    public void step(AgentManageContext context) {

        for (int i = 0; i < 10; i++) {
            for (UnitAddress unitAddress : context.querySurroundingUnits().getAll()) {
                context.queryPropertiesOf(unitAddress);
            }
        }

        if (energy == 10) {
            energy = 0;
            context.newAgent(LightReproducibleNotMigratingAgent.class);
        }

        energy++;

    }

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }
}
