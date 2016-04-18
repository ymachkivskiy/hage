package org.hage.platform.component.runtime.util;

import org.hage.platform.simulation.container.Stateful;

import java.util.Collection;

public class SimpleStatefulInitializerAndFinisher implements StatefulInitializer, StatefulFinisher {

    @Override
    public void performInitialization(Collection<? extends Stateful> statefuls) {
        statefuls.forEach(Stateful::init);
    }

    @Override
    public void performInitialization(Stateful stateful) {
        stateful.init();
    }

    @Override
    public void finish(Collection<? extends Stateful> statefuls) {
        statefuls.forEach(Stateful::finish);
    }

    @Override
    public void finish(Stateful stateful) {
        stateful.finish();
    }
}
