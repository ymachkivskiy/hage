package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.Unit;

@SingletonComponent
public class AgentUnitPostProcessPhase extends AbstractUnitPhase {

    @Override
    public String getPhaseName() {
        return "Agent unit post process";
    }


    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::postProcess;
    }
}
