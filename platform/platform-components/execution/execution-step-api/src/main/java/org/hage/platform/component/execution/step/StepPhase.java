package org.hage.platform.component.execution.step;

import java.util.Collection;

public interface StepPhase {
    String getPhaseName();

    // TODO: remove current step argument, use monitoring instead
    Collection<? extends Runnable> getRunnable(long currentStep);
}
