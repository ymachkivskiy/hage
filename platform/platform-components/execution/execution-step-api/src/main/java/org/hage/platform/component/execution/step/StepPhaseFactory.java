package org.hage.platform.component.execution.step;

import java.util.List;

public interface StepPhaseFactory {
    List<IndependentPhasesGroup> getFullCyclePhasesGroups();
}
