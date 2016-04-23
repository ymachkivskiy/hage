package org.hage.platform.component.execution.step;

import java.util.Collection;

public interface StepPhase {
    String getPhaseName();

    Collection<? extends Runnable> getRunnable(long currentStep);
}
