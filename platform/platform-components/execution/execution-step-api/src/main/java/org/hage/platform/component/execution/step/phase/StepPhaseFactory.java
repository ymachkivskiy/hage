package org.hage.platform.component.execution.step.phase;

import java.util.List;

public interface StepPhaseFactory {
    List<IndependentPhasesGroup> getFullCyclePhasesGroups();
}
