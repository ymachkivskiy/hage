package org.hage.platform.component.runtime.stepphase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.step.StepPhase;
import org.hage.platform.component.runtime.unit.Unit;
import org.hage.platform.component.runtime.unit.UnitActivationAware;
import org.hage.platform.component.runtime.unit.UnitDeactivationAware;
import org.hage.platform.component.structure.Position;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableCollection;

@Slf4j
abstract class AbstractUnitPhase implements StepPhase, UnitActivationAware, UnitDeactivationAware {

    private final Map<Position, Runnable> agentStepRunnableMap = new ConcurrentHashMap<>();

    @Override
    public final Collection<? extends Runnable> getRunnable(long currentStep) {
        return unmodifiableCollection(agentStepRunnableMap.values());
    }


    @Override
    public final void onUnitActivated(Unit unit) {
        Position unitPosition = unit.getPosition();

        log.debug("On unit on {}  activated.", unitPosition);

        agentStepRunnableMap.put(
            unitPosition,
            extractRunnable(unit));
    }

    @Override
    public final void onAgentsUnitDeactivated(Unit unit) {
        Position unitPosition = unit.getPosition();

        log.debug("On unit on {}  deactivated.", unitPosition);

        agentStepRunnableMap.remove(unitPosition);
    }

    protected abstract Runnable extractRunnable(Unit unit);
}
