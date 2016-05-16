package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.component.execution.step.phase.StepPhase;
import org.hage.platform.component.runtime.unit.Unit;
import org.hage.platform.component.runtime.unit.UnitActivationAware;
import org.hage.platform.component.runtime.unit.UnitDeactivationAware;
import org.hage.platform.component.structure.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableCollection;


abstract class AbstractUnitPhase implements StepPhase, UnitActivationAware, UnitDeactivationAware {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Position, Runnable> agentStepRunnableMap = new ConcurrentHashMap<>();

    @Override
    public Collection<? extends Runnable> getRunnable(long currentStep) {
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
