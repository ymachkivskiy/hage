package org.hage.platform.simulation.base;

import org.hage.platform.component.IStatefulComponent;

import java.io.Serializable;

public interface ControlAgent extends Serializable, IStatefulComponent {
    void step(ControlContext controlContext);
}
