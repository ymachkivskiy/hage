package org.hage.platform.component.execution.core.supplier;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;
import org.hage.platform.component.simulation.structure.SimulationCell;
import org.hage.platform.component.simulation.structure.event.SimulationStructureChangedEvent;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ListenableTasksChangeSupplier implements TasksChangeSupplier, EventSubscriber {

    private Map<SimulationCell, PhasedRunnable> translationMa; // TODO: 17.02.16  


    @Override
    public TasksChange pollCurrentChange() {
        return new TasksChange(null, null);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onStructureChanged(SimulationStructureChangedEvent event) {
        // TODO: 17.02.16
    }

}
