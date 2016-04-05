package org.hage.platform.component.runtime.execution.cycle;

import org.hage.platform.component.runtime.execution.PostStepPhase;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.asList;

public class FixedPhaseOrderOrderClassifier implements PostStepPhaseOrderClassifier {

    private final List<PostStepPhase> allowedPhasesInOrder;

    public FixedPhaseOrderOrderClassifier(PostStepPhase... allowedOrderedPhases) {
        this.allowedPhasesInOrder = asList(allowedOrderedPhases);
    }

    @Override
    public boolean isCorrectPhase(PostStepPhase phase) {
        return allowedPhasesInOrder.contains(phase);
    }

    @Override
    public int compare(PostStepPhase o1, PostStepPhase o2) {
        checkArgument(allowedPhasesInOrder.contains(o1), "Not registered phase " + o1 + " is being compared");
        checkArgument(allowedPhasesInOrder.contains(o2), "Not registered phase " + o2 + " is being compared");

        int i1 = allowedPhasesInOrder.indexOf(o1);
        int i2 = allowedPhasesInOrder.indexOf(o2);

        return i1 - i2;
    }

}
