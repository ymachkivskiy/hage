package org.hage.platform.component.runtime.stepphase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.Unit;

@Slf4j
@SingletonComponent
public class ControlAgentStepPhase extends AbstractUnitPhase {

    @Override
    public String getPhaseName() {
        return "Control agent step";
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::runControlAgent;
    }

}
