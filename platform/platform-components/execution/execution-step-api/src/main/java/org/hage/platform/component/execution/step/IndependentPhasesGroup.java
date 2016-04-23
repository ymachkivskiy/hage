package org.hage.platform.component.execution.step;

import lombok.Data;

import java.util.List;

@Data
public class IndependentPhasesGroup {
    private final List<StepPhase> phases;
}
