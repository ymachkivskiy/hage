package org.hage.platform.component.execution.core.supplier;

import org.hage.platform.component.execution.core.executor.ExecutionPhasesProvider;
import org.hage.platform.component.runtime.unit.UnitExecutionPhase;

import java.util.List;

import static java.util.Arrays.asList;

public class FixedExecutionPhasesProvider implements ExecutionPhasesProvider {

    private final List<UnitExecutionPhase> phases;

    public FixedExecutionPhasesProvider(UnitExecutionPhase... phases) {
        this.phases = asList(phases);
    }

    @Override
    public List<UnitExecutionPhase> getExecutionPhasesInOrder() {
        return phases;
    }

}
