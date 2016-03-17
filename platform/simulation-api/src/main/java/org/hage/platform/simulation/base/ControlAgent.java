package org.hage.platform.simulation.base;

import org.hage.platform.component.container.Stateful;

import java.io.Serializable;

public interface ControlAgent extends Serializable, Stateful {
    void step(ControlContext controlContext);
}
