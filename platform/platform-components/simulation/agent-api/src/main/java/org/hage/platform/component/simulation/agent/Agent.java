package org.hage.platform.component.simulation.agent;

import org.hage.platform.component.IStatefulComponent;

import java.io.Serializable;

public interface Agent extends Serializable, IStatefulComponent {
    void step(AgentSimulationContext executionContext);
}
