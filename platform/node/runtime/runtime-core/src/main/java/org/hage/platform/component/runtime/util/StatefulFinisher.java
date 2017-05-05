package org.hage.platform.component.runtime.util;

import org.hage.platform.simulation.container.Stateful;

import java.util.Collection;

public interface StatefulFinisher {
    void finish(Collection<? extends Stateful> statefuls);

    void finish(Stateful stateful);
}
