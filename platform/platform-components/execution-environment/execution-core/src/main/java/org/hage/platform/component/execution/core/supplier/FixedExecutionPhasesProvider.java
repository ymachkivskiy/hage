package org.hage.platform.component.execution.core.supplier;

import org.hage.platform.component.execution.core.executor.ExecutionPhasesProvider;

import java.util.List;

import static java.util.Arrays.asList;

public class FixedExecutionPhasesProvider implements ExecutionPhasesProvider {

    private final List<SimulationCellExecutionPhase> phases;

    public FixedExecutionPhasesProvider(SimulationCellExecutionPhase... phases) {
        this.phases = asList(phases);
    }

    @Override
    public List<SimulationCellExecutionPhase> getExecutionPhasesInOrder() {
        return phases;
    }

}
