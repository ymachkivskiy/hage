package org.hage.mocked.simdata.agent;

import org.hage.platform.simulation.runtime.agent.AgentManageContext;

public class HeavyNotReproducibleNotMigratingAgent extends LightNotReproducibleNotMigratingAgent {

    @Override
    public void step(AgentManageContext context) {
        super.step(context);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
