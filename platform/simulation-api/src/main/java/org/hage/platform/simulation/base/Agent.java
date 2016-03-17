package org.hage.platform.simulation.base;

import org.hage.platform.component.container.Stateful;

import java.io.Serializable;

public interface Agent extends Serializable, Stateful {
    void step(Context context);
}
