package org.hage.platform.simulation.runtime;

import org.hage.platform.simulation.container.Stateful;

import java.io.Serializable;

public interface ControlAgent extends Serializable, Stateful {
    void step(ControlContext controlContext);
}
