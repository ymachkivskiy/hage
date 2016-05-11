package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.Unit;

@SingletonComponent
public class UnitPropertiesUpdatePhase extends AbstractUnitPhase {

    @Override
    public String getPhaseName() {
        return "Unit properties update";
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::performStateChange;
    }

}
