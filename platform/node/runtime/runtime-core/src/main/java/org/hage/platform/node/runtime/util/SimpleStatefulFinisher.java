package org.hage.platform.node.runtime.util;

import org.hage.platform.simulation.container.Stateful;

import java.util.Collection;

public class SimpleStatefulFinisher implements  StatefulFinisher {

    @Override
    public void finish(Collection<? extends Stateful> statefuls) {
        statefuls.forEach(Stateful::finish);
    }

    @Override
    public void finish(Stateful stateful) {
        stateful.finish();
    }
}
