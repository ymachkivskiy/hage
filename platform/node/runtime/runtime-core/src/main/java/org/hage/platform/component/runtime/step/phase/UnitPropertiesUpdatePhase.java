package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.runtime.unit.Unit;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.PRE__UNIT_PROPERTIES_UPDATE;

@SingletonComponent
public class UnitPropertiesUpdatePhase extends AbstractUnitPhase {

    @Override
    public ExecutionPhaseType getType() {
        return PRE__UNIT_PROPERTIES_UPDATE;
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::performStateChange;
    }

}
