package org.hage.platform.node.execution.phase;

import java.util.Collection;

import static java.util.Collections.singletonList;

public abstract class SingleTaskExecutionPhase implements ExecutionPhase {

    @Override
    public final Collection<? extends Runnable> getTasks(long currentStep) {
        return singletonList(() -> execute(currentStep));
    }

    protected abstract void execute(long currentStep);

}
