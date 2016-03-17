package org.hage.platform.component.runtime;

import org.hage.platform.component.runtime.agent.AgentAdapter;
import org.hage.platform.simulation.base.Agent;
import org.springframework.stereotype.Component;

@Component
public class AgentAdapterCreator {

    public AgentAdapter createAdapterFor(Agent agent) {

        // TODO: to be implemented
        return new AgentAdapter(1, "some agent", agent);
    }

}
