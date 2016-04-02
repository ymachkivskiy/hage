package org.hage.platform.component.runtime.init;

import org.hage.platform.component.runtime.agent.AgentAdapter;
import org.hage.platform.simulation.runtime.Agent;
import org.springframework.stereotype.Component;

@Component
public class AgentAdapterCreator {

    public AgentAdapter createAdapterFor(Agent agent) {

        // TODO: to be implemented
        return new AgentAdapter(1, "some agent", agent);
    }

}
