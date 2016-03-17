package org.hage.platform.component.runtime.util;

import org.hage.platform.component.container.Stateful;

import java.util.Collection;

public class SimpleStatefulComponentsInitializer implements StatefulComponentsInitializer {

    @Override
    public void performInitialization(Collection<? extends Stateful> statefuls) {
        statefuls.forEach(Stateful::init);
    }

    @Override
    public void performInitialization(Stateful stateful) {
        stateful.init();
    }

}
