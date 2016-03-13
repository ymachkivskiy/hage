package org.hage.platform.component.execution.core.supplier;

import org.hage.platform.component.execution.core.executor.ExecutionPhasesProvider;
import org.hage.platform.component.simulation.structure.CellExecutionPhase;

import java.util.List;

import static java.util.Arrays.asList;

public class FixedExecutionPhasesProvider implements ExecutionPhasesProvider {

    private final List<CellExecutionPhase> phases;

    public FixedExecutionPhasesProvider(CellExecutionPhase... phases) {
        this.phases = asList(phases);
    }

    @Override
    public List<CellExecutionPhase> getExecutionPhasesInOrder() {
        return phases;
    }

}
