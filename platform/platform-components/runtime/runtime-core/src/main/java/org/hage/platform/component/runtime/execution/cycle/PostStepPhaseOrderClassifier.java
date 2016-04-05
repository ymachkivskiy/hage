package org.hage.platform.component.runtime.execution.cycle;

import org.hage.platform.component.runtime.execution.PostStepPhase;

import java.util.Comparator;

public interface PostStepPhaseOrderClassifier extends Comparator<PostStepPhase> {
    default boolean isCorrectPhase(PostStepPhase phase) {
        return true;
    }
}
