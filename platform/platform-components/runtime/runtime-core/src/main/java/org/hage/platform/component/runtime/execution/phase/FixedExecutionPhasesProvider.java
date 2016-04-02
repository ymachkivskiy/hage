package org.hage.platform.component.runtime.execution.phase;

import org.hage.platform.component.runtime.execution.ExecutionUnitPhase;

import java.util.List;

import static java.util.Arrays.asList;

public class FixedExecutionPhasesProvider implements ExecutionPhasesProvider {

    private final List<ExecutionUnitPhase> phases;

    public FixedExecutionPhasesProvider(ExecutionUnitPhase... phases) {
        this.phases = asList(phases);
    }

    @Override
    public List<ExecutionUnitPhase> getExecutionPhasesInOrder() {
        return phases;
    }

}
