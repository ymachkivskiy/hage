package org.hage.platform.component.execution.step;

public interface ExecutionStepRunnable extends Runnable {
    long getPerformedStepsCount();

    long getCurrentStepNumber();
}
