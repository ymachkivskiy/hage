package org.hage.platform.simulation.runtime.agent;

import org.hage.platform.simulation.container.Stateful;

import java.io.Serializable;

public interface Agent extends Serializable, Stateful {
    void step(AgentManageContext context);
}
