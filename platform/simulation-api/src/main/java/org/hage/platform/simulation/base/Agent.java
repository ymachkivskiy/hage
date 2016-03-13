package org.hage.platform.simulation.base;

import org.hage.platform.component.IStatefulComponent;

import java.io.Serializable;

public interface Agent extends Serializable, IStatefulComponent {
    void step(Context context);
}
