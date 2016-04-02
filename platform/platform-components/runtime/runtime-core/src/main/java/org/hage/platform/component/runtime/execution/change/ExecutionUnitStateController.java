package org.hage.platform.component.runtime.execution.change;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.ExecutionUnit;
import org.hage.platform.component.runtime.execution.phase.PhasedRunnable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Slf4j
@Component
class ExecutionUnitStateController implements TasksChangeSupplier, ActiveExecutionUnitsController {

    private Map<String, PhasedRunnable> translationMap = new HashMap<>();

    private List<ExecutionUnit> toBeRemoved = new ArrayList<>();
    private List<ExecutionUnit> toBeAdded = new ArrayList<>();


    @Override
    public synchronized TasksChange pollCurrentChange() {

        List<PhasedRunnable> toBeAdded = getToBeAdded();
        List<PhasedRunnable> toBeRemoved = getToBeRemoved();
        resetHolders();

        return new TasksChange(toBeAdded, toBeRemoved);
    }

    @Override
    public void activate(List<? extends ExecutionUnit> activatedUnits) {
        log.debug("Added units to activate {}", activatedUnits);
        toBeAdded.addAll(activatedUnits);
    }

    @Override
    public void deactivate(List<? extends ExecutionUnit> deactivatedUnits) {
        log.debug("Added units to deactivate {}", deactivatedUnits);
        toBeRemoved.addAll(deactivatedUnits);
    }

    private List<PhasedRunnable> getToBeAdded() {
        return toPhaseRunnable(toBeAdded);
    }

    private List<PhasedRunnable> getToBeRemoved() {
        List<PhasedRunnable> toBeRemoved = toPhaseRunnable(this.toBeRemoved);
        translationMap.keySet().removeAll(this.toBeRemoved);

        return toBeRemoved;
    }

    private void resetHolders() {
        log.debug("Clear toBeAdded '{}'", toBeAdded);
        log.debug("Clear toBeRemoved '{}'", toBeRemoved);

        toBeRemoved.clear();
        toBeAdded.clear();
    }

    private List<PhasedRunnable> toPhaseRunnable(List<ExecutionUnit> cells) {
        return cells
            .stream()
            .map(this::translate)
            .collect(toList());
    }

    private PhasedRunnable translate(ExecutionUnit executionUnit) {
        PhasedRunnable phasedRunnable = translationMap.get(executionUnit.getUnitId());

        if (phasedRunnable == null) {
            phasedRunnable = new CellPhaseRunnableAdapter(executionUnit);
            translationMap.put(executionUnit.getUnitId(), phasedRunnable);
        }

        return phasedRunnable;
    }


}
