package org.hage.platform.simulation.runtime.control;

import org.hage.platform.simulation.container.Stateful;

import java.io.Serializable;

public interface ControlAgent extends Serializable, Stateful {
    void step(ControlAgentManageContext controlContext);
}
