package org.hage.platform.simulation.runtime;

import org.hage.platform.simulation.container.Stateful;

import java.io.Serializable;

public interface Agent extends Serializable, Stateful {
    void step(Context context);
}
