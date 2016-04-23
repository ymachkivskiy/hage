package org.hage.platform.component.execution.step;

import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Data
public class IndependentPhasesGroup {
    private final List<StepPhase> phases;

    @Override
    public String toString() {
        return "Phases(" + phases.stream().map(StepPhase::getPhaseName).collect(joining(" ; ")) + ")";
    }
}
