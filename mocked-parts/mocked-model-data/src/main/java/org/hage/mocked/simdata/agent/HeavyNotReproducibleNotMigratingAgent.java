package org.hage.mocked.simdata.agent;

import org.hage.platform.simulation.runtime.agent.AgentManageContext;

public class HeavyNotReproducibleNotMigratingAgent extends LightNotReproducibleNotMigratingAgent {

    @Override
    public void step(AgentManageContext context) {
        super.step(context);

        int si = 0;
        for (int i = 0; i < 1000; i++) {
           si += i;
        }
    }
}
