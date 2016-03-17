package org.hage.platform.component.execution.core.supplier;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;
import org.hage.platform.component.runtime.event.SimulationStructureChangedEvent;
import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Slf4j
@Component
public class ListenableTasksChangeSupplier implements TasksChangeSupplier, EventSubscriber {

    private Map<ExecutionUnit, PhasedRunnable> translationMap = new HashMap<>();

    private List<ExecutionUnit> removed = new ArrayList<>();
    private List<ExecutionUnit> added = new ArrayList<>();


    @Override
    public synchronized TasksChange pollCurrentChange() {

        List<PhasedRunnable> toBeAdded = getToBeAdded();
        List<PhasedRunnable> toBeRemoved = getToBeRemoved();
        resetHolders();

        return new TasksChange(toBeAdded, toBeRemoved);
    }

    private List<PhasedRunnable> getToBeAdded() {
        return runnablesFor(added);
    }

    private List<PhasedRunnable> getToBeRemoved() {
        List<PhasedRunnable> toBeRemoved = runnablesFor(removed);
        translationMap.keySet().removeAll(removed);

        return toBeRemoved;
    }

    private void resetHolders() {
        log.debug("Clear added '{}'", added);
        log.debug("Clear removed '{}'", removed);

        removed.clear();
        added.clear();
    }

    private List<PhasedRunnable> runnablesFor(List<ExecutionUnit> cells) {
        return cells
            .stream()
            .map(this::translate)
            .collect(toList());
    }

    private PhasedRunnable translate(ExecutionUnit executionUnit) {
        PhasedRunnable phasedRunnable = translationMap.get(executionUnit);

        if (phasedRunnable == null) {
            phasedRunnable = new CellPhaseRunnableAdapter(executionUnit);
            translationMap.put(executionUnit, phasedRunnable);
        }

        return phasedRunnable;
    }


    @Subscribe
    @SuppressWarnings("unused")
    public synchronized void onStructureChanged(SimulationStructureChangedEvent event) {
        log.debug("Simulation structure changed: {}", event);

        removed.addAll(event.getRemovedUnits());
        added.addAll(event.getAddedUnits());
    }

}
