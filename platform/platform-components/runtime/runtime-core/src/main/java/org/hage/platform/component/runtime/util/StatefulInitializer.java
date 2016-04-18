package org.hage.platform.component.runtime.util;

import org.hage.platform.simulation.container.Stateful;

import java.util.Collection;

public interface StatefulInitializer {
    void performInitialization(Collection<? extends Stateful> statefuls);

    void performInitialization(Stateful stateful);
}
