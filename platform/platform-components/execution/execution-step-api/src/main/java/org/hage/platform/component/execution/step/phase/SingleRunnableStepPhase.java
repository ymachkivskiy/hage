package org.hage.platform.component.execution.step.phase;

import java.util.Collection;

import static java.util.Collections.singletonList;

public abstract class SingleRunnableStepPhase implements StepPhase {

    @Override
    public final Collection<? extends Runnable> getRunnable(long currentStep) {
        return singletonList(() -> executePhase(currentStep));
    }

    protected abstract void executePhase(long currentStep);

}
