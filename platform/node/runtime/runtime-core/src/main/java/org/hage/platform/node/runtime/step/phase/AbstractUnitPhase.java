package org.hage.platform.node.runtime.step.phase;

import org.hage.platform.node.execution.phase.ExecutionPhase;
import org.hage.platform.node.runtime.unit.Unit;
import org.hage.platform.node.runtime.unit.UnitActivationCallback;
import org.hage.platform.node.runtime.unit.UnitDeactivationCallback;
import org.hage.platform.node.structure.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableCollection;


abstract class AbstractUnitPhase implements ExecutionPhase, UnitActivationCallback, UnitDeactivationCallback {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Position, Runnable> agentStepRunnableMap = new ConcurrentHashMap<>();

    @Override
    public Collection<? extends Runnable> getTasks(long currentStep) {
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
